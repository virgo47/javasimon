netsh advfirewall firewall set rule name="WinRM-HTTP" new action=block
REM C:/windows/system32/sysprep/sysprep.exe /generalize /oobe /unattend:C:/Windows/Panther/Unattend/unattend.xml /quiet /shutdown
C:/windows/system32/sysprep/sysprep.exe /generalize /oobe /quiet /shutdown
