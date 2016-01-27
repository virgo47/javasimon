# Creating Windows 10 base image automatically

Based on: http://www.hurryupandwait.io/blog/creating-windows-base-images-for-virtualbox-and-hyper-v-using-packer-boxstarter-and-vagrant
His packer templates: https://github.com/mwrock/packer-templates
Problem: no Windows 10 template

## How to run it?

```
packer build -force vbox-win10ent.json
```

Or with different ISO (can be just different path, `iso_checksum` doesn't have to be specified):
```
packer build -force -var 'iso_url=file:///c:/work/iso-images/Windows10.iso' -var 'iso_checksum=d083c55ecb86158e3419032f4ed651e93e37c347' vbox-win10ent.json
```

When everything finishes, `windows10-virtualbox.box` will appear in the directory. Next steps
are all for Vagrant and starting with `init` they must be executed inside "environment" directory
containing `Vagrantfile` (may be empty before `init`):
* To add Vagrant box (`-f` overwrites existing one): `vagrant box add --force windows10-dev windows10-virtualbox.box`
* To initialize Vagrant environment directory, use: `vagrant init -mf windows10-dev`
* To remove any previous virtual machine: `vagrant destroy -f`
* To run the virtual machine: `vagrant up`
* Ant to stop it (can be shut down from inside, of course): `vagrant halt`
* Status: `vagrant status`

### Current problems

### What happens

After the unattended installation finishes, `boxstarter.ps1` takes over in Administrator `cmd.exe`
window. Packer console says: `Waiting for WinRM to become available...`

After `boxstarter.ps1` and subsequent `package.ps1` finish, `cmd` window may stay on (depending
whether `/k` or `/c` was used in `Autounattend.xml` command. In any case, packer console now
resumes with `Connected to WinRM!`.

If `guest_additions_mode` is set to `upload` (preferred is `attach`), there will be long running
`Uploading VirtualBox guest additions ISO...` which is OK, patience is needed. However, this may
fail with ugly error full of XML, because uploading big files via WinRM is fragile. Provision
script supports both modes, so leave it to `attach`.

After that it continues with provisioning script that does NOT show in the virtualbox Windows,
the output goes to packer console directly. When this all finishes, it shuts down using
`PackerShutdown.bat` (utilizing `postunattend.xml` and `SetupComplete.cmd` copied/renamed to
proper places by `provision.ps1`) and packs it to `*.box` file. From there see the Vagrant steps
mentioned above.

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
  <CommandLine>cmd.exe /c powershell.exe -ExecutionPolicy ByPass -File a:\boxstarter.ps1</CommandLine>
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

Script `boxstarter.ps1` is by Matt Wrock, any changes are at the start of it (setting
execution policy for 32/64 bit).

Script `package.ps1` required changes.
* To disable features, I used `dism` PowerShell module which is available everywhere, not just
on server versions. More about it [here](http://peter.hahndorf.eu/blog/WindowsFeatureViaCmd)
and how to use it [here](https://www.petri.com/getting-started-with-dism-powershell-cmdlets).
* ...

## Shutdown command

Shutdown is taken care of by `PackerShutdown.bat` which uses `sysprep`. Without `/unattend`
it does not "finish" the system properly and after first boot with Vagrant it starts asking
for settings, restarts and asks again.

Besides `/unattend` with provided `postunattend.xml` (copied from `a:` to harddisk by
`provision.ps1`) there is also `SetupComlete.cmd` that goes to `%WINDIR%\Setup\Scripts`.
Details [here](https://technet.microsoft.com/en-us/library/cc766314%28v=ws.10%29.aspx).

Simple `shutdown /s` would work too, but doesn't prepare the image properly (generalization?).

## Other ideas

* How to avoid one reboot after `vagrant up` with Finalizing setings?
* How to get rid of Networks sidebar (asking about PC to be discoverable)?
* How to uninstall cortana?
* Guest additions still not working.
* Disable OneDrive completely.
* Display file extensions in Win Explorer.
* In `provision.ps1` when removing `$env:windir\logs` failures occur because some logs are used by
another processes, permission problems or directories are not empty -- obviously something is
working in this dir:
```
    virtualbox-iso: +                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    virtualbox-iso: + CategoryInfo          : PermissionDenied: (C:\Users\vagran...mp\rvw5hshh.dll:FileInfo) [Remove-Item], Unauthoriz
    virtualbox-iso: edAccessException
    virtualbox-iso: + FullyQualifiedErrorId : RemoveFileSystemItemUnAuthorizedAccess,Microsoft.PowerShell.Commands.RemoveItemCommand
    virtualbox-iso: Remove-Item : Cannot remove item C:\Windows\logs\CBS\CBS.log: The process cannot access the file 'CBS.log' because it
    virtualbox-iso: is being used by another process.
    virtualbox-iso: At C:\Windows\Temp\script.ps1:37 char:5
    virtualbox-iso: +                 Remove-Item $_ -Recurse -Force | Out-Null
    virtualbox-iso: +                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    virtualbox-iso: + CategoryInfo          : WriteError: (CBS.log:FileInfo) [Remove-Item], IOException
    virtualbox-iso: + FullyQualifiedErrorId : RemoveFileSystemItemIOError,Microsoft.PowerShell.Commands.RemoveItemCommand
    virtualbox-iso: Remove-Item : Cannot remove item C:\Windows\logs\CBS: The directory is not empty.
    virtualbox-iso: At C:\Windows\Temp\script.ps1:37 char:5
    virtualbox-iso: +                 Remove-Item $_ -Recurse -Force | Out-Null
    virtualbox-iso: +                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    virtualbox-iso: + CategoryInfo          : WriteError: (CBS:DirectoryInfo) [Remove-Item], IOException
    virtualbox-iso: + FullyQualifiedErrorId : RemoveFileSystemItemIOError,Microsoft.PowerShell.Commands.RemoveItemCommand
    virtualbox-iso: Remove-Item : Cannot remove item C:\Windows\logs\WindowsUpdate: The directory is not empty.
    virtualbox-iso: At C:\Windows\Temp\script.ps1:37 char:5
    virtualbox-iso: +                 Remove-Item $_ -Recurse -Force | Out-Null
    virtualbox-iso: +                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    virtualbox-iso: + CategoryInfo          : WriteError: (WindowsUpdate:DirectoryInfo) [Remove-Item], IOException
    virtualbox-iso: + FullyQualifiedErrorId : RemoveFileSystemItemIOError,Microsoft.PowerShell.Commands.RemoveItemCommand
    virtualbox-iso: Remove-Item : Cannot remove item C:\Windows\logs: The directory is not empty.
    virtualbox-iso: At C:\Windows\Temp\script.ps1:37 char:5
    virtualbox-iso: +                 Remove-Item $_ -Recurse -Force | Out-Null
    virtualbox-iso: +                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    virtualbox-iso: + CategoryInfo          : WriteError: (C:\Windows\logs:DirectoryInfo) [Remove-Item], IOException
    virtualbox-iso: + FullyQualifiedErrorId : RemoveFileSystemItemIOError,Microsoft.PowerShell.Commands.RemoveItemCommand
    virtualbox-iso: Remove-Item : Cannot remove item C:\Windows\winsxs\manifestcache: The directory is not empty.
    virtualbox-iso: At C:\Windows\Temp\script.ps1:37 char:5
    virtualbox-iso: +                 Remove-Item $_ -Recurse -Force | Out-Null
    virtualbox-iso: +                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    virtualbox-iso: + CategoryInfo          : WriteError: (C:\Windows\winsxs\manifestcache:DirectoryInfo) [Remove-Item], IOException
    virtualbox-iso: + FullyQualifiedErrorId : RemoveFileSystemItemIOError,Microsoft.PowerShell.Commands.RemoveItemCommand
```