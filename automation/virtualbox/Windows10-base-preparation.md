# Base image for Windows 10 on VirtualBox

Based on: http://huestones.co.uk/node/305
Some command sequences copied here for convenience.

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