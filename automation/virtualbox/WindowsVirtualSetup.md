# Windows VirtualBox Setup

Using the right tools (Packer/Vagrant) it does not have to be only VirtualBox. So far, the
most advanced resources related to automation with Vagrant/Packer are by Matt Wrock:

* Using Vagrant (older): http://www.hurryupandwait.io/blog/in-search-of-a-light-weight-windows-vagrant-box
* Packer/Boxstarter (newer): http://www.hurryupandwait.io/blog/creating-windows-base-images-for-virtualbox-and-hyper-v-using-packer-boxstarter-and-vagrant
* Sources (packer templates): https://github.com/mwrock/packer-templates
* Finished Vagrant boxes: https://atlas.hashicorp.com/mwrock

Note: In further text we will reference `$VAGRANT_HOME`, this variable may not be set and then
defaults to `~/.vagrant.d`.

## Starting with published Vagrant boxes

Easiest way is to start from Matt's Vagrant boxes. See the notes added directly
[for the box](). Example (git-bash):
```
cd ~/vagrant-environment
mkdir windows-nano
cd windows-nano
vagrant init mwrock/WindowsNano
vagrant up
```

The last command downloads the image if it's not already cached locally in `$VAGRANT_HOME`
and starts the virtual machine. In some cases the screen is visible (for Windows Nano it is),
for others it may not (some Linux boxes are build as headless). In any case you may check running
virtual machines (when `VirtualBox` installation directory is on `PATH`):
```
VBoxManage list runningvms
# or the same with vagrant:
vagrant status

# and later to shutdown it (requires WinRM and GuestAdditions):
vagrant halt
# or if it refuses:
vagrant halt -f
```

## Importing built Vagrant box

Let's say we used Packer or something else to build `windows.box` for Vagrant.
To import it so Vagrant knows about it:
```
vagrant box add /c/work/tools/vagrant-boxes/windows.box --name windowsXY
```

This stores the box and necessary metainformation in `$VAGRANT_HOME`, subdirectory `boxes`
and then name of the box (`windowsXY` in the example). With this name we can now init and
up/halt it just like in the examples above.


## Exporting Vagrant box to a file

This step is needed when you want to share the box with someone using traditional methods like
flashdrive, etc. There are two basic ways how to do this -- `vagrant package` and
`vagrant box repackage`. First one can be executed in two ways:

* Easiest way -- from the specific "environment" directory with Vagrantfile: `vagrant package`
* From anywhere you need to specify base, which is the name of the VirtualBox, not the Vagrant box:
```
VBoxManage.exe list vms
# find the right name and:
vagrant package --base name_of_vm_xy
```
* Finally it can be done with box repackage: `vagrant box repackage windowsXY virtualbox 0`

In all cases `package.box` is created, this can be customzied with additional option
`--output windowsxy.box` for `vagrant package` (`vagrant box repackage` does not have this option).


## Getting rid of the provided box

This will leave box in the `$VAGRANT_HOME` (to get it from there use `vagrant box remove`) just
removes the provisioned environments. Go to environment directory and run:
```
vagrant destroy
```
Optionally can be run with -f to force it without asking. You can further delete the `Vagrantfile`
and the directory if needed.


## Making guest accessible from host

First, we need host-only interface on the host. TODO: Needs double-check, and probably DHCP settings.
```
# lists existing host-only interfaces, these appear in host's ip/ifconfig
VBoxManage.exe list hostonlyifs
# if no 'VirtualBox Host-Only Ethernet Adapter' exists, add one (default name is good)
VBoxManage.exe hostonlyif create
# if you accidentally added more and/or you want to remove one, use:
VBoxManage.exe hostonlyif remove 'VirtualBox Host-Only Ethernet Adapter #2'
# and this adds NIC to the virtual machine using host-only with default name
VBoxManage.exe modifyvm winserver2012-dev --nic2 hostonly
```