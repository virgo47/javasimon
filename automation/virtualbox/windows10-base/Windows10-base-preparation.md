# Base image for Windows 10 on VirtualBox

Most of this is manual. Matt Wrock's work referenced [here](../windows2012-server-base/Windows2012-server-preparation.md)
is much better fit for what we want.

Based on: http://huestones.co.uk/node/305
Some command sequences copied here for convenience.

Other ideas:
* http://www.hurryupandwait.io/blog/in-search-of-a-light-weight-windows-vagrant-box
* https://github.com/majkinetor/posher

This actually contains most of the process using PowerShell commands, but it's Win2012 server.
Discussion contains some gist/script references, tips to disable USB driver before packaging the
box, etc.

## Part 1 & 2

No problem, mostly manual. *I guess it can be partially executed with commands.*

## Part 3

**Before UAC Disable download some other browser, because with UAC disabled IE/Edge won't start.**
About UAC disable/enable with command: http://superuser.com/questions/227860

First is enable (1), second to disable (0). In any case, restart is required.
```
reg ADD HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Policies\System /v EnableLUA /t REG_DWORD /d 1 /f
reg ADD HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Policies\System /v EnableLUA /t REG_DWORD /d 0 /f
```

Up to UAC Disable, no problem. *Again, can this be made with commands?* This section ends with:
```
reg add HKEY_LOCAL_MACHINE\Software\Microsoft\Windows\CurrentVersion\Policies\System /v EnableLUA /d 0 /t REG_DWORD /f /reg:64
```
RESTART

Configuration of WinRM service starts with `winrm quickconfig -q` command that fails, if connection
profile is `Public`. Hence the first powershell command is added (run from `cmd`):
```
powershell -Command "Get-NetConnectionProfile | Set-NetConnectionProfile -NetworkCategory Private"

winrm quickconfig -q
winrm set winrm/config/winrs @{MaxMemoryPerShellMB="512"}
winrm set winrm/config @{MaxTimeoutms="1800000"}
winrm set winrm/config/service @{AllowUnencrypted="true"}
winrm set winrm/config/service/auth @{Basic="true"}
sc config WinRM start= auto

powershell -Command "Set-ExecutionPolicy -ExecutionPolicy Unrestricted"
```

Then from "Enable remote connection to your box" we're back to manual...

Running `sdelete.exe` may seem to get stuck, just focus to cmd and press enter to "push it". :-)

## Part 4

Vagrantfile is next to this MD file. Do the following in this directory:
```
# this creates ~5GB windows.box file
vagrant package --base "Windows 10" --output c:\work\tools\vagrant-boxes\windows.box --vagrantfile Vagrantfile
# this creates vagrant box in VAGRANT_HOME (~/.vagrant.d), roughly the same size + some metadata
vagrant box add c:\work\tools\vagrant-boxes\windows.box --name windows10
```

Now go to another dedicated "environment" directory for this box. First time it is an empty dir:
```
# see: https://docs.vagrantup.com/v2/cli/init.html
vagrant init windows10
```

Now we may configure it a bit more: https://docs.vagrantup.com/v2/virtualbox/configuration.html

Otherwise it will have some tmp/random name, etc. When we're done editing the `Vagrantfile`, it's
time to start it up. All the following commands also have to be executed from aforementioned
"environment" directory:
```
# this will take a lot of time - but only for the first time
vagrant up
# now to check that the box is up and running (VirtualBox UI shows the same)
vagrant status
# and to connect - use .\vagrant as the name, forgetting the domain part causes login failure
vagrant rdp
```

...and BTW, while the RDP session is running it seems that Ctrl+V (copy/paste in general) is messy,
e.g. it doesn't work on your local machine (like now in IDEA). See here for more:
http://www.gfi.com/blog/copy-paste-working-remote-desktop-connection-whats-wrong/