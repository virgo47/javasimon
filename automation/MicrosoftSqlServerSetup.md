Starting from fresh virtual with [Windows 10](virtualbox/windows10-base/Windows10-base-packer.md)
we can use `choco` and similar tools. Our goal is to automate process of installing SQL database,
initialize the instance, etc.

## Installation and first test

In the Boxstarter shell:
```dos.bat
cinst -y mssqlserver2014express
```

This installs local instance `SQLEXPRESS`. To connect:
```dos.bat
sqlcmd
# or more explicitly
sqlcmd -E -S .\SQLEXPRESS
```

In SQL then, we will create our user we will be connecting to:
```sql
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
```powershell
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
```powershell
# Load the assemblies
[reflection.assembly]::LoadWithPartialName("Microsoft.SqlServer.Smo")
[reflection.assembly]::LoadWithPartialName("Microsoft.SqlServer.SqlWmiManagement")
```
This can be actually run from the PowerShell open by failed `sqlps` as it leaves us in the PS
prompt. It can be run even if it does not fail, that means it can be run anytime just for sure.

### Enable TCP/IP and set the port

```powershell
# For locally run script we can determine the comptuter name automatically, otherwise set it here
$compname = (get-item env:\computername).Value
$instname = 'SQLEXPRESS' # or default MSSQLSERVER

$smo = 'Microsoft.SqlServer.Management.Smo.'
$wmi = new-object ($smo + 'Wmi.ManagedComputer').

# Enable the TCP protocol on the default instance.
$uri = "ManagedComputer[@Name='$compname']/ ServerInstance[@Name='$instname']/ServerProtocol[@Name='Tcp']"
$Tcp = $wmi.GetSmoObject($uri)
$Tcp.IsEnabled = $true
$TcpIpAll = $Tcp.IPAddresses["IPAll"]
$TcpIpAll.IPAddressProperties["TcpPort"].Value ="1433"
$Tcp.Alter()

# This just creates var with default instance, we can use it for tests later
$DfltInstance = $Wmi.Services["$instname"]
```

### Change authentication mode

```powershell
$srv = new-object ($smo + 'Server') @($null, "$compname\$instname")[$DfltInstance -eq $null] 
$srv.Settings.LoginMode = [Microsoft.SqlServer.Management.SMO.ServerLoginMode]::Mixed
$srv.Alter()
```

Construct `@(false-result, true-result)[condition]` uses PowerShell array and boolean coercion
into index (0, 1). Actually only `@(true-result)[condition-negated]` would be enough in case
of `$null` for false result, but condition inversion may be even trickier to read than construct
with array of false/true.

### Restart MS SQL Server

```powershell
# repeat the lines from previous script all the way to the $instname definition

# Get a reference to the default instance of the Database Engine.
$instprefix = @($null, 'MSSQL$')[$DfltInstance -eq $null]
$inst = $Wmi.Services["$instprefix$instname"]
$inst.Stop();
do {
	"Waiting for stopped"
	Start-Sleep -Seconds 2
	$inst.Refresh()
} while ($inst.ServiceState -ne "Stopped") 
$inst # display stopped instance (not necessary) 
$inst.Start();
do {
	"Waiting for stopped"
	Start-Sleep -Seconds 2
	$inst.Refresh()
} while ($inst.ServiceState -ne "Running")
$inst # show results, not necessary
```

### Enable and start SQL Browser

This is necessary if we don't use the default instance.

```powershell
if (-not $DfltInstance) {
	Set-Service SqlBrowser -startuptype "automatic"
	Start-Service SqlBrowser
}
```

### Allow ports in firewall

```powershell
netsh advfirewall firewall add rule name=SQLPort dir=in protocol=tcp action=allow localport=1433 remoteip=localsubnet profile=PUBLIC
if (-not $DfltInstance) {
	netsh advfirewall firewall add rule name=SQLUdpPort dir=in protocol=udp action=allow localport=1434 remoteip=localsubnet profile=PUBLIC
}
```
You may change profile to DOMAIN or PRIVATE and remoteip can also by customized, but localsubnet
will work in VirtualBox host -> guest scenario using hostonly LAN.

After this, check your IP with `ipconfig` (e.g. on Ethernet 2, if `hostonly` is set for network
adapter #2). We can telnet to this IP from the host machine using port 1433. If we don't use the
default SQL instance, opening UDP port 1434 is required as well. Now we can try SQL Management
Studio, use `<guest-IP>\SQLEXPESS`, SQL Server Authentication, login `test` and passord.


## Customize installation parameters

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

All this is probably irrelevant in case of manual installation.

## Alternative with default instance

We can use `cinst -y mssql2014express-defaultinstance` which uses SQL Server Express, but the
only instance acts as a default one. This one does not have `sqlcmd` in path, we have to add
it or use `"c:\Program Files\Microsoft SQL Server\Client SDK\ODBC\110\Tools\Binn\SQLCMD.EXE"`.

The rest is resolved in scripts using conditions based on `$DfltInstance`.

## Handy SQL commands

To connect to a remote SQL server (if `\INSTANCE` is skipped, it defaults to MSSQLSERVER):
```
sqlcmd -S 192.168.30.101\SQLEXPRESS -U test
```

When using `sqlcmd` you need to use GO (batch terminator) on separate line to run the command/batch.
```sql
-- returns current database
select db_name()
-- current schema
select schema_name()

-- current user, there are other options: original_login(), suser_sname()
-- see http://stackoverflow.com/questions/4101863/sql-server-current-user-name
select current_user -- e.g. dbo
select system_user -- Windows user, e.g. WIN-XY\vagrant
-- list schemas and their owners (cast to set column widths)
select cast(schema_name as varchar(20)), cast(schema_owner as varchar(20)) from information_schema.schemata
-- list databases: http://stackoverflow.com/q/2087902/658826
exec sp_databses -- this requires some permissions and may return empty result if missing
select * from sys.sysdatabases
-- list roles (1), users (0)
select name from sysusers where issqlrole = 1 -- 0 for users
-- list logins
select name from syslogins

-- create a database: https://msdn.microsoft.com/en-us/library/ms176061.aspx
create database DBNAME
-- drop (cannot be in use)
drop database DBNAME
use DBNAME
-- creating user for a login in a database (must be the current database)
create user test for login test with default_schema=dbo
-- adding test to db_owner role, so it can do anything
alter role db_owner add member test
```

## WIP: Adding SSL for SQL server

TODO: Find the way to do this with a script.

* Seems I need to install certificate first. I tried:
http://www.careexchange.in/how-to-install-certificate-authority-on-windows-server-2012/
  * In step 2: without Web Enrolment, that seems to be for self-service
  * Step 3: TODO (on home virtual)