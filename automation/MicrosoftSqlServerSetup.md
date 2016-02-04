Starting from fresh virtual with [Windows 10](virtualbox/windows10-base/Windows10-base-packer.md)
we can use `choco` and similar tools. Our goal is to automate process of installing SQL database,
initialize the instance, etc.

In the Boxstarter shell:
```
cinst -y mssqlserver2014express
```

This installs local instance `SQLEXPRESS`. To connect:
```
sqlcmd -E -S .\SQLEXPRESS
```

TODO: database, user, login
```
create login test with password = '...', check_expiration=off, check_policy=off;
GO
```

## Database connectivity

We will enable TCP/IP on our SQL instance, set up 1433 static port for listening, change
authentication mode for SQL Server and add allow rule for this port to firewall.

Following [this howto](https://technet.microsoft.com/en-us/library/dd206997%28v=sql.105%29.aspx)
and [this covers port too](http://blog.dbi-services.com/sql-server-2012-configuring-your-tcp-port-via-powershell/)
and [this one mentiones UDP firewall port](http://blog.citrix24.com/configure-sql-express-to-accept-remote-connections/)
for SQL Browser.

### Potential problems with sqlps

Runnign `sqlps` failed with:
```
import-module : File C:\Program Files (x86)\Microsoft SQL Server\120\Tools\PowerShell\Modules\SQLPS\Sqlps.ps1 cannot
be loaded because running scripts is disabled on this system. For more information, see about_Execution_Policies at
http://go.microsoft.com/fwlink/?LinkID=135170.
At line:1 char:1
+ import-module SQLPS  -DisableNameChecking
+ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    + CategoryInfo          : SecurityError: (:) [Import-Module], PSSecurityException
    + FullyQualifiedErrorId : UnauthorizedAccess,Microsoft.PowerShell.Commands.ImportModuleCommand
```
...and I couldn't make it run whatever execution policy I set. But we can make classic PowerShell
to imitate `sqlps` starting with the following lines:
```
# Load the assemblies
[reflection.assembly]::LoadWithPartialName("Microsoft.SqlServer.Smo")
[reflection.assembly]::LoadWithPartialName("Microsoft.SqlServer.SqlWmiManagement")
```

### Enable TCP/IP and set the port

```
$smo = 'Microsoft.SqlServer.Management.Smo.'
$wmi = new-object ($smo + 'Wmi.ManagedComputer').

# For locally run script we can determine the comptuter name automatically, otherwise set it here
$compname = (get-item env:\computername).Value
$instname = 'SQLEXPRESS' # or default MSSQLSERVER

# Enable the TCP protocol on the default instance.
$uri = "ManagedComputer[@Name='$compname']/ ServerInstance[@Name='$instname']/ServerProtocol[@Name='Tcp']"
$Tcp = $wmi.GetSmoObject($uri)
$Tcp.IsEnabled = $true
$TcpIpAll = $Tcp.IPAddresses["IPAll"]
$TcpIpAll.IPAddressProperties["TcpPort"].Value ="1433"
$Tcp.Alter()
```

### Change authentication mode

```
$srv = new-object ($smo + 'Server') $compname\$instname
$srv.Settings.LoginMode = [Microsoft.SqlServer.Management.SMO.ServerLoginMode]::Mixed
$srv.Alter()
```

### Restart MS SQL Server

```
# repeat the lines from previous script all the way to the $instname definition

# Get a reference to the default instance of the Database Engine.
$DfltInstance = $Wmi.Services["MSSQL`$$instname"]
$DfltInstance.Stop();
do {
	"Waiting for stopped"
	Start-Sleep -Seconds 2
	$DfltInstance.Refresh()
} while ($DfltInstance.ServiceState -ne "Stopped") 
$DfltInstance # refresh the cache and display (not necessary) 
$DfltInstance.Start();
do {
	"Waiting for stopped"
	Start-Sleep -Seconds 2
	$DfltInstance.Refresh()
} while ($DfltInstance.ServiceState -ne "Running")
# Refresh the cache and display the state of the service.
$DfltInstance.Refresh(); $DfltInstance
```

### Enable and start SQL Browser

```
Set-Service SqlBrowser -startuptype "automatic"
Start-Service SqlBrowser
```

### Allow ports in firewall

```
netsh advfirewall firewall add rule name=SQLPort dir=in protocol=tcp action=allow localport=1433 remoteip=localsubnet profile=PUBLIC
netsh advfirewall firewall add rule name=SQLUdpPort dir=in protocol=udp action=allow localport=1434 remoteip=localsubnet profile=PUBLIC
```
You may change profile to DOMAIN or PRIVATE and remoteip can also by customized, but localsubnet
will work in VirtualBox host -> guest scenario using hostonly LAN.

After this, check your IP with `ipconfig` (e.g. on Ethernet 2, if `hostonly` is set for network
adapter #2). With this 

## Customize install parameters

* http://stackoverflow.com/questions/25246720/install-sql-server-using-powershell
* https://msdn.microsoft.com/en-us/library/ms144259.aspx

Referenced `setup.exe` seems to be this one:
`C:\Program Files\Microsoft SQL Server\120\Setup Bootstrap\SQLServer2014\setup.exe`

But the setup arguments must be provided for `cinst` command. Later it just keeps saying that
the medium is corrupt. Default arguments used by choco package `MsSqlServer2014Express v12.0.4100.1`
are `/IACCEPTSQLSERVERLICENSETERMS /Q /ACTION=install /INSTANCEID=SQLEXPRESS /INSTANCENAME=SQLEXPRESS /UPDATEENABLED=FALSE`.
This can all be found in `chocolatey.log`. It may be handy to use `MSSQLSERVER` as instance id/name
because that one is default for other tools, but there is no problem with SQLEXPRESS either, you
just need to state it explicitly after hostname (e.g. in Management studio, for _Server name_ use
`dbhost_or_IP\SQLEXPRESS`).