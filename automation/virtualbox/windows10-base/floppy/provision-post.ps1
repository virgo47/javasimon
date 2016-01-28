# TODO: Disabled because now we still have some errors in the script (marked by TODO)
# in this script no interaction is possible, so no press any key or Inquire, etc.
#$ErrorActionPreference = "Stop"

Write-Host "Post-sysprep steps"

# classic-shell, first registry init
regedit /s a:\classicshell-init.reg

# cleaning pinned-to-tasklist applications - this normally works, but does not have effect after vagrant up
Remove-Item "$Env:AppData\Microsoft\Internet Explorer\Quick Launch\User Pinned\TaskBar\*"
Remove-Item "HKCU:\Software\Microsoft\Windows\CurrentVersion\Explorer\Taskband"

Write-Host 'Provision script FINISHED';
