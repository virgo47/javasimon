Starting from fresh virtual with [Windows 10](virtualbox/windows10-base/Windows10-base-packer.md)
we can use `choco` and similar tools. Our goal is to automate process of installing SQL database,
initialize the instance, etc.

In the Boxstarter shell:
```
cinst -y mssqlserver2014express

```

Next probably:
* http://stackoverflow.com/questions/25246720/install-sql-server-using-powershell
* https://msdn.microsoft.com/en-us/library/ms144259.aspx

Referenced `setup.exe` seems to be this one:
`C:\Program Files\Microsoft SQL Server\120\Setup Bootstrap\SQLServer2014\setup.exe`
