Enable-RemoteDesktop
Get-NetConnectionProfile | Set-NetConnectionProfile -NetworkCategory Private
netsh advfirewall firewall add rule name="Remote Desktop" dir=in localport=3389 protocol=TCP action=allow

Update-ExecutionPolicy -Policy Unrestricted

Write-BoxstarterMessage "Removing unused features..."
Import-Module dism
Get-WindowsOptionalFeature -online | where {$_.State -eq 'disabled'} | Disable-WindowsOptionalFeature -Remove -online

Write-BoxstarterMessage "Installing Windows Updtates..."
Install-WindowsUpdate -AcceptEula

# Remove modern apps
Get-AppxPackage -Name *WindowsCamera* | Remove-AppxPackage
Get-AppxPackage -Name *ZuneMusic* | Remove-AppxPackage
Get-AppxPackage -Name *WindowsMaps* | Remove-AppxPackage
Get-AppxPackage -Name *MicrosoftSolitaireCollection* | Remove-AppxPackage
Get-AppxPackage -Name *BingFinance* | Remove-AppxPackage
Get-AppxPackage -Name *ZuneVideo* | Remove-AppxPackage
Get-AppxPackage -Name *BingNews* | Remove-AppxPackage
Get-AppxPackage -Name *WindowsPhone* | Remove-AppxPackage
Get-AppxPackage -Name *Windows.Photos* | Remove-AppxPackage
Get-AppxPackage -Name *BingSports* | Remove-AppxPackage
Get-AppxPackage -Name *XboxApp* | Remove-AppxPackage
Get-AppxPackage -Name *BingWeather* | Remove-AppxPackage
Get-AppxPackage -Name *WindowsSoundRecorder* | Remove-AppxPackage
Get-AppxPackage -Name *3DBuilder* | Remove-AppxPackage
Get-AppxPackage -Name *SkypeApp* | Remove-AppxPackage
Get-AppxPackage -Name *MicrosoftOfficeHub* | Remove-AppxPackage

# removing Show Task View icon
Set-ItemProperty -Path 'HKCU:\Software\Microsoft\Windows\CurrentVersion\Explorer\Advanced' -Name 'ShowTaskViewButton' -Value 0
# disabling Cortana
New-Item 'HKCU:\Software\Policies\Microsoft\Windows\Windows Search' -Force | New-ItemProperty -Name 'AllowCortana' -PropertyType 'DWord' -Value 0 -Force | Out-Null
New-Item 'HKCU:\Software\Microsoft\Windows\CurrentVersion\Search' -Force | New-ItemProperty -Name  'SearchboxTaskbarMode' -PropertyType 'DWord' -Value 0 -Force | Out-Null
# TODO: cleaning pinned-to-tasklist applications - this normally works, but does not have effect after vagrant up
Remove-Item "$Env:AppData\Microsoft\Internet Explorer\Quick Launch\User Pinned\TaskBar\*"
Remove-Item "HKCU:\Software\Microsoft\Windows\CurrentVersion\Explorer\Taskband"

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