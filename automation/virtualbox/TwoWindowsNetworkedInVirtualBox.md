# How to create virtual network with VirtualBox

Let's say I want to create mini-network with two computers:
* Windows Server 2012 acting as a domain controller,
* Windows 10 as a member of domain.

## Networking

We need to choose the right way of networking from: https://www.virtualbox.org/manual/ch06.html


### Internal networking

Very easy, for both machines select in VBox **Settings/Network**, tab **Adapter 1** networking type
(**Attached to**) of **Internal Network** and choose the same name, e.g. `testdomain`. In this case
host is not accessible from the guests.
 
Start both machines and use `ipconfig` to find their IPs and try to ping them. Out of box I could
ping Windows 10 from the other, not the other way around, because Windows Server 2012 has disabled
inbound ping. Follow [this](http://blog.blksthl.com/2012/11/20/how-to-enable-ping-in-windows-server-2012/)
to fix the problem or just use these PowerShell commands:

```
Import-Module NetSecurity
Set-NetFirewallRule -DisplayName "File and Printer Sharing (Echo Request – ICMPv4-In)" -enabled True
```


### Host-only Adapter
 
Works similar like Internal networking, but host and guests are on the same network. Using
`ipconfig` on the host we can find **Ethernet adapter VirtualBox Host-Only Network** and see its
IP. Name of the adapter can be different, there can be multiple adapters as well -- this can be
set in **File/Preferences/Network**, tab **Host-only Networks**.

Note: it seems there is no way how to specify name of the host-only network and even command
line tool `VBoxManage hostonlyif create` merely creates network with fixed name and sequence
number in it. This makes it extremely ugly for "lab" testing.


### Problem VERR_INTNET_FLT_IF_NOT_FOUND

We've got VirtualBox (5.0.10r104061) with `winserver2012` virtual machine in it. I created new
Host-only Network (File/Preferences, Network, tab Host-only Networks, button +) and used this
network for vitual machine (click on `winserver2012`, Settings, Network, tab Adapter 2, Enable,
Attached to: Host-only Adatper, Name: only one possible). During start of the virtual machine
following error occurs:
```
Failed to open a session for the virtual machine winserver2012_default_1454188297542_9145.

Failed to open/create the internal network 'HostInterfaceNetworking-VirtualBox Host-Only Ethernet Adapter' (VERR_INTNET_FLT_IF_NOT_FOUND).

Failed to attach the network LUN (VERR_INTNET_FLT_IF_NOT_FOUND).

Result Code: E_FAIL (0x80004005)
Component: ConsoleWrap
Interface: IConsole {872da645-4a9b-1727-bee2-5585105b9eed}
```

I'm not sure whether it's 5.0.10 error, but I upgraded to to 5.0.14, restarted and it still
repeated. I removed the hostonly network and added it again, and the problem disappeared. However,
the new default hostonly network was setup without DHCP, this needs to be set up, typically with
the same netaddress (mask 255.255.255.0), with 100 for DHCP server, and IP pool 101-254. If the
guest is running, it requires shutdown and boot again (restart doesn't seem to work, it will not
see new DHCP). After this the hostonly networking seems running.

How to make this all in one sweep, I have no idea.


### Outside connectivity

If we want to leave NAT networking so the machines are not cut from the outside world (handy
for browsing, any downloads, updates, etc.) we may leave **Adapter 1** as is and set-up Internal
Network (or Host-only Adapter) on **Adapter 2** (or vice versa).


## Installing Domain Controller

In following steps we will follow recipes from Active Directory Cookbook, 3rd edition (ADC from now
on).

* Prepare vanilla Windows Server 2012R2 using Packer and start it up with Vagrant.
* Server should have static IP on the domain network (host-only or internal type in VirtualBox).
IP on NAT adapter can be still from DHCP, no problem.
* Create new forest -- ADC 2.1 (using Server Manager, just more Next than described), in case
of failure because of "source path" just repeat the procedure. We probably rely Windows Update
servers here, but I couldn't find what *alternate source path* is working even using original
installation media in virtual DVD drive.
  * After installation restart will be requested, after restart flag in the *Server Manager*
  indicates next steps: *Promote this server to a domain controller* (This diverges from ADC 2.1
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