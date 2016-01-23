# Creating Windows 10 base image automatically

Based on: http://www.hurryupandwait.io/blog/creating-windows-base-images-for-virtualbox-and-hyper-v-using-packer-boxstarter-and-vagrant
His packer templates: https://github.com/mwrock/packer-templates
Problem: no Windows 10 image

## How to run it?

```
packer build -force vbox-win10ent.json
```

Current problems:
* it won't shut down - probably the shutdown script is not right?
* packer never finishes, stays forever on `==> virtualbox-iso: Waiting for WinRM to become available...`

## Autounattend.xml

* Generate it here: http://windowsafg.no-ip.org/win10x86_x64.html
* Remove `<ProductKey>----</ProductKey>` from the wrong sections (component
`Microsoft-Windows-Shell-Setup`), you may use `<ProductKey><WillShowUI>OnError</WillShowUI></ProductKey>`
in `UserData` sections, but it's not necessary.
* Place it in `floppy/Autounattend.xml`

## Packer template

TODO