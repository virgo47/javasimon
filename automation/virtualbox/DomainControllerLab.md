# Building yourself Windows Domain lab

Let's say I want to create mini-network with two computers:
* Windows Server 2012 acting as a domain controller,
* Windows 10 as a member of domain.

For networking, see [this page](TwoWindowsNetworkedInVirtualBox.md).

For the next steps I used recipes from *Active Directory Cookbook, 3rd edition* (ADC3 from now on)
and *Active Directory, 5th edition* book (AD5).


## Installing Domain Controller

This is generally covered in AD5, chapter 9, Domain Controllers/Building Domain Controllers. ADC3
is actually a bit obsolete (like obsolete `dcpromo`), I'll just mention related recipies, but AD5
chapter is better.

* Prepare vanilla Windows Server 2012R2 using Packer and start it up with Vagrant.
* Server should have static IP on the domain network (host-only or internal type in VirtualBox).
IP on NAT adapter can be still from DHCP, no problem.
* Create new forest -- ADC3, recipe 2.1 (using Server Manager, just more Next than described),
in case of failure because of "source path" just repeat the procedure. We probably rely Windows
Update servers here, but I couldn't find what *alternate source path* is working even using
original installation media in virtual DVD drive.
  * After installation restart will be requested, after restart flag in the *Server Manager*
  indicates next steps: *Promote this server to a domain controller* (This diverges from ADC3, 2.1
  recipe which recommends `dcpromo`, but that just prints advice to use Server Manager. :-))
  * In the wizard choose *Add a new forest*, domain name `naive.test`
  * Leave functional levels, DNS server yes, leave disabled options, set DSRM password (vagrant).
  * DNS delegation may fail, go on.
  * NetBIOS domain name results in `NAIVE`.
  * Leave paths as suggested.
  * Review also generates PowerShell script (see lower).
  * Prerequisites Check repeats DNS warnings and also complains about non-static IP on some
  adapters, which is OK as the NAT adapter really is non-static.
  * Install. Server restarts at the end.

```
#
# Windows PowerShell script for AD DS Deployment
#

Import-Module ADDSDeployment
Install-ADDSForest `
-CreateDnsDelegation:$false `
-DatabasePath "C:\Windows\NTDS" `
-DomainMode "Win2012R2" `
-DomainName "naive.test" `
-DomainNetbiosName "NAIVE" `
-ForestMode "Win2012R2" `
-InstallDns:$true `
-LogPath "C:\Windows\NTDS" `
-NoRebootOnCompletion:$false `
-SysvolPath "C:\Windows\SYSVOL" `
-Force:$true
```


### Add NAIVE\vagrant to Domain Admins group!

Although the user set up the domain controller, he is still only local admin. :-) But he can add
himself into Domain Admins group. Without this we can't change Global Policy, e.g. Password Policy.


### Setting IP for host-only network

TODO - UNTESTED: In PowerShell:
Also read: http://www.adminarsenal.com/admin-arsenal-blog/using-powershell-to-set-static-and-dhcp-ip-addresses-part-1/

```
$ifix = get-wmiobject win32_networkadapter -filter "netconnectionid = 'Ethernet 2'" | 
  select -expandproperty InterfaceIndex
Set-NetIPAddress –InterfaceIndex $ifix –IPAddress 192.168.151.10
```


## Joining another computer to the domain

Firstly, the computers must see each other. To do that, set host-only network interface on
another computer (call it *vagrant1*) to static IP in the same network, e.g. 192.168.151.101,
with DNS IP set to domain controller.

Create Computer account on the DC for the new computer (e.g. `vagrant1`).

Then on the computer change the name and domain for the computer, which will request domain admin
user/password (NAIVE\vagrant). Done.

To be precise, this means **Start**, right-click **This PC**, click **Change settings**
in *Computer name...* section and click **Change** button. TODO: What exactly do I have to eneter? `naive.test`? `NAIVE`?

What to do when previous computer with the same name does not exist? E.g. `vagrant1` was used
as a name for previous (now expired) virtual box. See how to [remove computer from domain](https://technet.microsoft.com/en-us/library/dd277423.aspx).

Problem: ["duplicate (potentially the shorter NETBIOS) name exists on the network"](https://social.technet.microsoft.com/Forums/office/en-US/711f3433-cd1b-4783-bb49-9287e4240212/duplicate-potentially-the-shorter-netbios-name-exists-on-the-network-win2008r2?forum=winservergen)
`nbtstat -RR` does not help. Following [this discussion](https://social.technet.microsoft.com/Forums/office/en-US/711f3433-cd1b-4783-bb49-9287e4240212/duplicate-potentially-the-shorter-netbios-name-exists-on-the-network-win2008r2?forum=winservergen)
indicates that we need to:

* remove computer from a domain (see above)
* remove the server from AD
* remove from DNS
