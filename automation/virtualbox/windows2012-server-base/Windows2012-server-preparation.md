# Base image for Windows 2012 Server on VirtualBox

Most important resources related to automation (Vagrant/Packer) by Matt Wrock:

* Using Vagrant: http://www.hurryupandwait.io/blog/in-search-of-a-light-weight-windows-vagrant-box
* Packer/Boxstarter: http://www.hurryupandwait.io/blog/creating-windows-base-images-for-virtualbox-and-hyper-v-using-packer-boxstarter-and-vagrant
* Sources: https://github.com/mwrock/packer-templates
* Finished Vagrant boxes: https://atlas.hashicorp.com/mwrock

This is actually copy/based on Matt Wrock's packer template for Windows Server 2012

## Raw ISO - not good

* Download from: https://www.microsoft.com/en-us/evalcenter/evaluate-windows-server-2012-r2
* Choose VHD, this can be used with VirtualBox directly.
* **Copy** downloaded VHD to some dedicated directory for VirtualBox media, e.g. Disks.
It cannot be put into a directory that will become new VirtualBox machine. We copy, so we
can start over from fresh without the need to download it again.
* Create **New** machine, name **Windows Server 2012 DC** (or similar), VBox will automatically
set type to **Windows 2012 (64-bit)**. Choose **Use an existing virtual hard disk file**
and browse to the copied VHD file.

**Ctrl+Alt+Del** in VirtualBox = Host key (**Compose** in my case) + **Del**

Further reading:

* Setup: http://www.itpro.co.uk/desktop-software/20510/windows-server-2012-installation-and-setup

## Packer/Boxstarter way


### Preparation steps to install Packer via Chocolatey

In Administrator's `cmd`:
```
@powershell -NoProfile -ExecutionPolicy Bypass -Command "iex ((new-object net.webclient).DownloadString('https://chocolatey.org/install.ps1'))"
```

Close/reopen Administrator's `cmd`:
```
choco install packer -y
```

## Various 

Disable password policy: http://stackoverflow.com/questions/23260656/modify-local-security-policy-using-powershell
```
secedit /export /cfg c:\secpol.cfg
(gc C:\secpol.cfg).replace("PasswordComplexity = 1", "PasswordComplexity = 0") | Out-File C:\secpol.cfg
secedit /configure /db c:\windows\security\local.sdb /cfg c:\secpol.cfg /areas SECURITYPOLICY
rm -force c:\secpol.cfg -confirm:$false
```

Disable password expiration:
```
NET accounts /MAXPWAGE:UNLIMITED
```