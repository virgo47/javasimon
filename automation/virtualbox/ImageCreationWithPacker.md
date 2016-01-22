# Image creation with Packer

Let's start with [Packer](https://www.packer.io/) summary:

> Packer is a tool for creating machine and container images for multiple platforms from
> a single source configuration.

## Packer vs Vagrant

Both can provision virtual machines. And both are made by [HashiCorp](https://www.hashicorp.com/)
that offers other OOS solutions and commercial [Atlas](https://www.hashicorp.com/atlas.html). 

### Description comparison

[Vagrant](https://www.vagrantup.com/) summary says:

> Create and configure lightweight, reproducible, and portable development environments.

Short descriptions from [Products page](https://www.hashicorp.com/index.html#products).

**Packer:**

> Packer is a tool for creating images for platforms such as Amazon AWS, OpenStack, VMware,
> VirtualBox, Docker, and more — all from a single source configuration.
>
> Common use cases for Packer include creating appliances, pre-baking deployment images,
> packaging applications, and testing configuration management scripts.

[Documentation](https://www.packer.io/docs/)

**Vagrant:**

> Development environments made easy. Vagrant creates and configures lightweight, reproducible,
> and portable development environments with support for Virtualbox, VMware, Hyper-V, Docker,
> and more. Eliminate long onboarding times and "works on my machine" excuses.

[Documentation](https://www.vagrantup.com/docs/)

### What are the differences?

Packer creates images for various platforms (including VirtualBox), Vagrant focuses on development
environment, which includes VirtualBox machines. [But](http://stackoverflow.com/questions/16647069/should-i-use-vagrant-or-docker-for-creating-an-isolated-environment?rq=1#comment34031731_22370529):

> Vagrant is not for managing machines, Vagrant is for managing development environments.
> The fact Vagrant spins up machines is mostly historic. The next version of Vagrant has first
> class support to spin up dev environment using Docker as a provider directly on the host or
> any VM (Mac, Win). It can also spin up raw LXC if thats what someone wants (again, on the host
> or VM). Vagrant is interested in doing what is best to create a portable development environment,
> whether that means creating a VM or not. (March 2014, Mitchell Hashimoto)

Some discussion is also in [comments here](http://stackoverflow.com/q/17733063/658826), e.g.:

> Packer uses provisioners to set up a base image, while Vagrant uses provisioners to set up
> a dev environment on top of such an base image.

Especially using of the same provisioners make it a bit confusing, but important points are:

* You start with Packer to get your base image.
* Then you use Vagrant for development convenience.

But Packer is userful also for other scenarios (production, testing) without Vagrant involved.

Mitchell Hashimoto, author of both tools, explains on the [mailing list](https://groups.google.com/d/msg/packer-tool/4lB4OqhILF8/NPoMYeew0sEJ):

> First off, Packer does not require or even encourage Vagrant usage. Packer stands on its own as
> a useful tool for its own purposes: namely, building machine images for whatever environment
> you require. 
>
> However, Packer is very useful alongside Vagrant, if you want. Packer can create Vagrant boxes.
> So if you wanted Vagrant boxes, then Packer can do this for you. This is the ONLY way Packer
> helps you use Vagrant. It does not REPLACE any part of Vagrant.

## Practical Packer tips

Packer downloads ISOs into `packer_cache`. This cache prevents Packer to redownload bit ISO that
is already available. By design this cache appears in working directory to prevent surprises,
but in case we need cache for various builds run from different directories, we can use
environment variable `PACKER_CACHE_DIR`. ([more](https://github.com/mitchellh/packer/pull/804))

## Demo with Windows Nano

```
git clone https://github.com/mwrock/packer-templates.git
cd packer-templates
packer build -force -only virtualbox-iso vbox-nano.json
```

This runs for a long time, don't interact with it, even when it asks for login (which would
be `vagrant`/`vagrant`, but packer will stop it on its own eventually and creates
`windowsNano-virtualbox.box`, which is a Vagrant box. (During the last phase there will be
VirtualBox process running on background for a couple of minutes, it's OK.)