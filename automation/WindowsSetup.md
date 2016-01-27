# Faster setup of new Windows

Start with Chocolatey package system - in *administrative* cmd:
```
@powershell -NoProfile -ExecutionPolicy Bypass -Command "iex ((new-object net.webclient).DownloadString('https://chocolatey.org/install.ps1'))"
```

To find out what is installed using Chocolatey, use `choco list -l` or shortcut `clist -l`.
Without `-l` it prints all available packages. Using `clist -li` prints also applications
that are not installed using Chocolatey, but could have been.

We can install and download virtually any other favourite tool (lines with `#` comments run
in PowerShell/Boxstarter Shell, in `cmd` you need to strip the end):
```
cinst -y 7zip.commandline
cinst -y notepad2
cinst -y notepadreplacer -installarguments '/notepad=C:\Progra~1\Notepad2\Notepad2.exe /verysilent'
cinst -y gnuwin32-coreutils.portable

cinst -y firefox

# developer's tools, probably for host, not for virtual guest
cinst -y git
cinst -y vagrant
cinst -y packer
cinst -y StrawberryPerl
```

Untested:
```
cinst -y unzip
cinst -y vim
cinst -y rapidee
cinst -y classic-shell

cinst -y Handle
cinst -y procexp

cinst -y GoogleChrome
cinst -y FoxitReader

cinst -y ConEmu
cinst -y putty
cinst -y winscp
cinst -y TotalCommander

cinst -y gpg4win-light
cinst -y tortoisesvn
cinst -y openvpn-community
cinst -y ruby
cinst -y licecap
cinst -y virtualbox
cinst -y nodejs
cinst -y k-litecodecpackfull
cinst -y ghc # Haskell
cinst -y gimp
cinst -y foobar2000
```

Notes:
* ghc is haskell
* gpg4win-light -- I'm not sure here, there is also Gpg4win, but can light be enough?
* notepadreplacer, never tried it: https://chocolatey.org/packages/notepadreplacer