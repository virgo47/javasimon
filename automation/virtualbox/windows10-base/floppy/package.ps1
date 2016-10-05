Enable-RemoteDesktop
Get-NetConnectionProfile | Set-NetConnectionProfile -NetworkCategory Private
netsh advfirewall firewall add rule name="Remote Desktop" dir=in localport=3389 protocol=TCP action=allow

Update-ExecutionPolicy -Policy Unrestricted

Write-BoxstarterMessage "Removing unused features..."
Import-Module dism
Get-WindowsOptionalFeature -online | where {$_.State -eq 'disabled'} | Disable-WindowsOptionalFeature -Remove -online

Write-BoxstarterMessage "Installing Windows Updtates..."
Install-WindowsUpdate -AcceptEula

Write-BoxstarterMessage "Removing page file"
$pageFileMemoryKey = "HKLM:\SYSTEM\CurrentControlSet\Control\Session Manager\Memory Management"
Set-ItemProperty -Path $pageFileMemoryKey -Name PagingFiles -Value ""

if (Test-PendingReboot) { Invoke-Reboot }

# Remove modern apps
$apps = @(
	"WindowsCamera", "ZuneMusic", "WindowsMaps", "MicrosoftSolitaireCollection",
	"BingFinance", "ZuneVideo", "BingNews", "WindowsPhone", "Windows.Photos", "BingSports",
	"XboxApp", "Microsoft.Office.OneNote", "Microsoft.CommsPhone", "Microsoft.Messaging"
	"BingWeather", "WindowsSoundRecorder", "3DBuilder", "SkypeApp", "MicrosoftOfficeHub",
	"Microsoft.People", "Microsoft.Office.Sway", "Microsoft.Getstarted", "Microsoft.Appconnector")
foreach ($app in $apps) {
	Write-BoxstarterMessage "Removing: $app"
	Get-AppxPackage -Name "*$app*" | Remove-AppxPackage
    Get-AppxProvisionedPackage -Online | where-object {$_.packagename -like "*$app*"} | Remove-AppxProvisionedPackage -Online
}

Write-BoxstarterMessage "Setting up winrm"
netsh advfirewall firewall add rule name="WinRM-HTTP" dir=in localport=5985 protocol=TCP action=allow

Enable-PSRemoting -Force -SkipNetworkProfileCheck
Enable-WSManCredSSP -Force -Role Server
winrm set winrm/config/client/auth '@{Basic="true"}'
winrm set winrm/config/service/auth '@{Basic="true"}'
winrm set winrm/config/service '@{AllowUnencrypted="true"}'