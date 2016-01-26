Enable-RemoteDesktop
Get-NetConnectionProfile | Set-NetConnectionProfile -NetworkCategory Private
netsh advfirewall firewall add rule name="Remote Desktop" dir=in localport=3389 protocol=TCP action=allow

Update-ExecutionPolicy -Policy Unrestricted

Write-BoxstarterMessage "Removing unused features...DISABLED"
Import-Module dism
#Get-WindowsOptionalFeature -online | where {$_.State -eq 'disabled'} | Disable-WindowsOptionalFeature -Remove -online

Write-BoxstarterMessage "Installing Windows Updtates...DISABLED"
#Install-WindowsUpdate -AcceptEula

Write-BoxstarterMessage "Removing page file"
$pageFileMemoryKey = "HKLM:\SYSTEM\CurrentControlSet\Control\Session Manager\Memory Management"
Set-ItemProperty -Path $pageFileMemoryKey -Name PagingFiles -Value ""

if(Test-PendingReboot){ Invoke-Reboot }

Write-BoxstarterMessage "Setting up winrm"
netsh advfirewall firewall add rule name="WinRM-HTTP" dir=in localport=5985 protocol=TCP action=allow

Enable-PSRemoting -Force -SkipNetworkProfileCheck
Enable-WSManCredSSP -Force -Role Server
winrm set winrm/config/client/auth '@{Basic="true"}'
winrm set winrm/config/service/auth '@{Basic="true"}'
winrm set winrm/config/service '@{AllowUnencrypted="true"}'