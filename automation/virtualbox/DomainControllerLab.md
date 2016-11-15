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

TODO: Script it!


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
another computer (call it *vagrant1*) to **static IP in the same network, e.g. 192.168.151.101,
with DNS IP set to domain controller**.

```
$ifix = get-wmiobject win32_networkadapter -filter "netconnectionid = 'Ethernet 2'" | 
  select -expandproperty InterfaceIndex
Set-NetIPAddress –InterfaceIndex $ifix –IPAddress 192.168.151.101
Set-DnsClientServerAddress -InterfaceIndex $ifix -ServerAddresses ("192.168.151.10")
```

This does not seem necessary: Create Computer account on the DC for the new computer
(e.g. `vagrant1`).

Then on the computer change the name and domain for the computer: **Start**, right-click
**This PC**, click **Change settings** in *Computer name...* section and click **Change** button.
Set computer name (e.g. `vagrant1`) and domain name (e.g. `naive.test`). Next use the domain admin
user (`NAIVE\vagrant`) and their password. Restart required.

WARNING: If full domain name (as you can see on the DC server under **Active Directory Users and
Computers**) cannot be contacted, double-check that the added computer has static IP **and DNS IP
is set to DC server!** Even if the first step seems to work with short domain name (like `NAIVE`)
it may actually go against different domain (maybe your normal company one).

PowerShell command (password entered interactively):
```
$domain = "naive.test"
$username= "$domain\vagrant"
Add-Computer -DomainName $domain -Credential $username
Restart-Computer
```

Using password in script (not secure, but OK for a lab):
```
$domain = "naive.test"
$username= "$domain\vagrant"
$password = "Vagrant123" | ConvertTo-SecureString -asPlainText -Force
$credential = New-Object System.Management.Automation.PSCredential($username,$password)
Add-Computer -DomainName $domain -Credential $credential
Restart-Computer
```


## Removing the computer from a domain

When needed we can use `Remove-Computer` to leave the domain. Using `$credentials` from previous
scripts:
```
Remove-Computer -Credential $credential -Force
Restart-Computer
```

Note: Both `Add-Computer` and `Remove-Computer` first fail if the computer is already added/removed
to/from a domain and **after this** the credentials are checked. You cannot check credentials
(e.g. password) repeating the same action if the state is wrong, you must use the action that makes
sense.


## Various problems when DNS to DC is not set

Various problems appeared because I forgot to set DNS IP on the computer about to join the domain.
(This probably tried to use other domains on the network?) Among others:

* "duplicate (potentially the shorter NETBIOS) name exists on the network"
* Domain Controller could not be contacted
* unknown domain when using full name (e.g. `naive.test`)


## Problem - Status Conflict in nbtstat -n

On the domain controller:
```
PS C:\Windows\system32> nbtstat -n

Ethernet:
Node IpAddress: [10.0.2.15] Scope Id: []

    No names in cache

Ethernet 2:
Node IpAddress: [192.168.151.10] Scope Id: []

                NetBIOS Local Name Table

       Name               Type         Status
    ---------------------------------------------
    NAIVE          <00>  GROUP       Registered
    NAIVE-DC1      <00>  UNIQUE      Conflict
    NAIVE          <1C>  GROUP       Registered
    NAIVE-DC1      <20>  UNIQUE      Conflict
    NAIVE          <1B>  UNIQUE      Registered
```

Some ideas [here](https://social.technet.microsoft.com/Forums/windowsserver/en-US/07ec9419-e2d6-4745-b65c-249b0931cf4b/duplicate-name-on-network-nbtstat-n-duplicate-user?forum=winservergen),
but I didn't know how to use it.

Listing domain controllers in my domain:
```
PS C:\Windows\system32> nltest /dclist:naive.test
Get list of DCs in domain 'naive.test' from '\\NAIVE-DC1.naive.test'.
    NAIVE-DC1.naive.test [PDC]  [DS] Site: Default-First-Site-Name
The command completed successfully
```

This seems to be OK. This disappeared after a while on its own (no idea) I didn't change anything
except for using (presumably) get/read commands above.


## Problem with DNS - ID 4013 Warning in AD DS Events

May be caused by previous conflict? Posed no problem after the conflict went away.