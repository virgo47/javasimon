# Creating Windows 10 base image automatically

Based on: http://www.hurryupandwait.io/blog/creating-windows-base-images-for-virtualbox-and-hyper-v-using-packer-boxstarter-and-vagrant
His packer templates: https://github.com/mwrock/packer-templates
Problem: no Windows 10 image

## How to run it?

```
packer build -force vbox-win10ent.json
```

Current problems:
* in virtual there is some bug with 0ing empty space, wget fails (try again in existing windows10-dev box)
* check the rest of the `package.ps1` script, there were more errors following
* after shutdown packer console says:
```
virtualbox-iso: Error uploading guest additions: Error uploading file to $env:TEMP\winrmcp-56a548af-0f3f-99e5-f731-e73094cc79bf.tmp:
Couldn't create shell: http error: 500 - <s:Envelope xml:lang="en-US" xmlns:s="http://www.w3.org/2003/05/soap-envelope"
xmlns:a="http://schemas.xmlsoap.org/ws/2004/08/addressing" xmlns:x="http://schemas.xmlsoap.org/ws/2004/09/transfer"
xmlns:e="http://schemas.xmlsoap.org/ws/2004/08/eventing" xmlns:n="http://schemas.xmlsoap.org/ws/2004/09/enumeration"
xmlns:w="http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd" xmlns:p="http://schemas.microsoft.com/wbem/wsman/1/wsman.xsd">
<s:Header><a:Action>http://schemas.dmtf.org/wbem/wsman/1/wsman/fault</a:Action><a:MessageID>uuid:BE878E8E-C519-404E-A165-A739ED0731BD
</a:MessageID><a:To>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</a:To>
<a:RelatesTo>uuid:5c76855b-0647-41ab-51f0-87d46fb6c772</a:RelatesTo></s:Header><s:Body><s:Fault>
<s:Code><s:Value>s:Receiver</s:Value><s:Subcode><s:Value>w:InternalError</s:Value></s:Subcode></s:Code>
<s:Reason><s:Text xml:lang="en-US">Server execution failed </s:Text></s:Reason><s:Detail>
<f:WSManFault xmlns:f="http://schemas.microsoft.com/wbem/wsman/1/wsmanfault" Code="2148007941" Machine="127.0.0.1">
<f:Message><f:ProviderFault provider="Shell cmd plugin" path="%systemroot%\system32\winrscmd.dll">Server execution failed 
</f:ProviderFault></f:Message></f:WSManFault></s:Detail></s:Fault></s:Body></s:Envelope>
```

## Autounattend.xml

Prepared `Autounattend.xml` is in `floppy` directory and will be available in `A:\` within
virtual box when the installation starts. This completely removes human interaction.

### How to get it

* Generate it here: http://windowsafg.no-ip.org/win10x86_x64.html
* Remove `<ProductKey>----</ProductKey>` from the wrong sections (component
`Microsoft-Windows-Shell-Setup`). You may use `<ProductKey><WillShowUI>OnError</WillShowUI></ProductKey>`
in `UserData` sections, but it's not necessary.
* You may remove all sections with `processorArchitecture="x86"` for 64-bit installation, or with
`processorArchitecture="amd64"` for 32-bit system. You may leave both though if you plan install
both Windows versions.
* Add section that runs "provisioning" script (on Windows 10 it's important to bypass execution
policy):
```
<SynchronousCommand wcm:action="add">
  <CommandLine>cmd.exe /c C:\Windows\System32\WindowsPowerShell\v1.0\powershell.exe -ExecutionPolicy ByPass -File a:\boxstarter.ps1</CommandLine>
  <Order>1</Order>
</SynchronousCommand>
```
* Remove all others `SynchronousCommands` or reorder them appropriately. Preferably we will
do everything else out of `Autounattend.xml`.
* Place it in `floppy/Autounattend.xml`.

### Important values

`Username` and `Password/Value` must be `vagrant` (lowercase), descriptions and display names are
not that important. If we mix various case for username, we may end up with two user accounts.

## Packer template

TODO

## PowerShell scripts

Script `boxstarter.ps1` is by Matt Wrock, no changes.

Script `package.ps1` required changes.
* To disable features, I used `dism` PowerShell module which is available everywhere, not just
on server versions. More about it [here](http://peter.hahndorf.eu/blog/WindowsFeatureViaCmd)
and how to use it [here](https://www.petri.com/getting-started-with-dism-powershell-cmdlets).
* ...

## Other ideas

* How to avoid one reboot after `vagrant up` with Finalizing setings?
* How to get rid of Network sidebar?
* How to uninstall cortana?
* Still not finished with guest additions, seems that provision.ps1 is necessary for this? Probably
the ISO is not available before.
* How to make Vagrant virtualbox run on foreground?