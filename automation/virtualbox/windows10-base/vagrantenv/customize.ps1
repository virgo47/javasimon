# classic-shell, first registry init
regedit /s c:\Vagrant\classicshell-init.reg

# cleaning pinned-to-tasklist applications - this normally works, but does not have effect after vagrant up
Remove-Item "$Env:AppData\Microsoft\Internet Explorer\Quick Launch\User Pinned\TaskBar\*" -Force
Remove-Item "HKCU:\Software\Microsoft\Windows\CurrentVersion\Explorer\Taskband" -Force

# how to say Firefox it's a default browser and skip question about import from IE?
