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

This would take roughly ~30 mins on my i5 machine (with updates/SxS/optimizing/zeroing), resulting
box-file is 3.8GB. With everything on it takes still under 60 mins (most of it is SxS cleanup),
with box-file size 4.6GB (is the size bigger because of the updates?). In any case, vagrant steps
are a matter of 5 minutes top, most of it taken by the initial `vagrant up`.

When everything finishes, `windows10-virtualbox.box` will appear in the directory. Next steps
are all for Vagrant and starting with `init` they must be executed inside "environment" directory
containing `Vagrantfile` (may be empty before `init`):
* To add Vagrant box (`-f` overwrites existing one): `vagrant box add --force windows10-dev windows10-virtualbox.box`
* To initialize Vagrant environment directory, use: `vagrant init -mf windows10-dev`
* To remove any previous virtual machine: `vagrant destroy -f`
* To run the virtual machine: `vagrant up`
* Ant to stop it (can be shut down from inside, of course): `vagrant halt`
* Status: `vagrant status`

First `vagrant up` takes longer as it has to create virtual machine and machine itself is
fresh and needs longer to configure and restart during initial boot. Subsequent runs are faster.

Also - I build the box with GUI enabled. Another option is keep it disabled and use RDP, which
still works: `vagrant rdp` (use `.\vagrant` as a name, just `vagrant` may not work!)

...and BTW, while the RDP session is running it seems that Ctrl+V (copy/paste in general) is messy,
e.g. it doesn't work on your local machine (like now in IDEA). See more
[here](http://www.gfi.com/blog/copy-paste-working-remote-desktop-connection-whats-wrong/).

### Current problems

* Some registry settings made in package/provision.ps1 (e.g. unpinning or ClassicShell setup)
does not appear in the finished box. They are the right commands, but all get "reverted".

## Packer process overview

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
* Remove all `<ProductKey>----</ProductKey>` -- they are in wrong sections and not necessary
anyway.
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
On the other hand, some people use it in their packer templates.

## Vagrantfile

For VirtualBox customize options, see: https://www.virtualbox.org/manual/ch08.html

## Other ideas

* How to avoid one reboot after `vagrant up` with Finalizing setings? This would allow me to
add further customizations that are "nuked" during sysprep (probably), e.g. removing pinned
programs from the tasklist, etc.
* How to get rid of Networks sidebar (asking about PC to be discoverable)?
* Disable OneDrive completely.
* Display file extensions in Win Explorer.
* Do I want to eject guest additions CD? Do I want CD drive at all?
I tried `vboxmanage_post` section, which works, but also leads to this error (nonfatal though):
```
==> virtualbox-iso: Exporting virtual machine...
    virtualbox-iso: Executing: export packer-virtualbox-iso-1453981832 --output output-virtualbox-iso\packer-virtualbox-iso-1453981832.ovf
==> virtualbox-iso: Error unregistering guest additions: VBoxManage error: VBoxManage.exe: error: No storage device attached to device slot 0 on port 1 of controller 'IDE Controller'
==> virtualbox-iso: VBoxManage.exe: error: Details: code VBOX_E_OBJECT_NOT_FOUND (0x80bb0001), component SessionMachine, interface IMachine, callee IUnknown
==> virtualbox-iso: VBoxManage.exe: error: Context: "DetachDevice(Bstr(pszCtl).raw(), port, device)" at line 393 of file VBoxManageStorageController.cpp
==> virtualbox-iso: Unregistering and deleting virtual machine...
```
* How to pin/unpin programs with a command? Best with exe path, but needs to be done later,
something refreshes it (sysprep?).
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
* Go over other `windows packer` or specifically `windows 10 packer` projects on GitHub. That
can teach me new tricks. :-)
* Experiment in `vbox-win10ent-alt.json` tries to shutdown with sysprep using `windows-restart`
provisioner, then to run other part of the provision (now in `setup-complete.ps1`) and finally
shutdown it again. Doesn't work, as it complains that first windows-restart command ends with
exit code 1. Experiment `vbox-win10ent-test.json` is the part before sysprep, so I can experiment
with it. But calling sysprep works just fine, so I don't know what is the problem (does sysprep
returns exit code 1?).
