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