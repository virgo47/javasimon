# Faster setup of new Windows

Start with Chocolatey package system - in *administrative* cmd:
```
@powershell -NoProfile -ExecutionPolicy Bypass -Command "iex ((new-object net.webclient).DownloadString('https://chocolatey.org/install.ps1'))"
```

By default Chocolatey downloads into TEMP directory, which results in repeated downloads, when
you experiment a lot with the same package. You may point it to a specific directory like this:
```
choco config set cacheLocation 'c:\ProgramData\Chocolatey\tmp-cache'
```
Also [see here](https://github.com/chocolatey/choco/wiki/How-To-Change-Cache). As a result
you have to take care of the directory and maybe clean it up sometimes. You can also pack this
cache directory and move it elsewhere to do offline installation.
(!) TODO: check how this works, because [there is more to it](http://stackoverflow.com/questions/18528919/how-to-install-chocolatey-packages-offline).

To allow packages without checksums (avoiding prompt every time):
```
choco feature enable -n allowEmptyChecksums
```

To find out what is installed using Chocolatey, use `choco list -l` or shortcut `clist -l`.
Without `-l` it prints all available packages. Using `clist -li` prints also applications
that are not installed using Chocolatey, but could have been.

We can install and download virtually any other favourite tool (lines with `#` comments run
in PowerShell/Boxstarter Shell, in `cmd` you need to strip the end and replace `'` with `"`
as needed):
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
cinst -y wireshark # instal WinPcap in advance manually, seems the chocolatey package is broken
cinst -y fsviewer
```
                                                                      
Notes:
* ghc is haskell
* gpg4win-light -- I'm not sure here, there is also Gpg4win, but can light be enough?

## ConEmu settings and tips

* Go to Settings `Win+Alt+P`
* In **Keys & Macro**:
	* Global hotkey for *Minimize/Restore* `` Ctrl+` `` collides with IDEA, change to `<None>`
(using `Win+2` anyway).
	* Switch to previous/next console change to `Alt+Left/Right`
	* Open new console popup is `Win+N` (good)
	* In **Keyboard** subscreen uncheck **Win+Number - activate console**.
* In **Features** check **Inject ConEmuHk** to support colors in shells properly
* Settings XML can be placed next to `conemu.exe` and will be loaded instead of registry
* Set it [as default term] (even if we run `cmd` from Start it will use ConEmu). Go to
**Integration**, **Default term** and check first checkboxes (Force..., Register..., Leave in TSA).

### Problem - Pin to task bar for Admin

If we pin `powershell` to the task bar, it will start in ConEmu, but if we change its properties
**Shortcut/Advanced...** and check **Run as administrator** it will not use ConEmu anymore.
On the other hand, if we add `-new_console:a` after the command in **Shortcut**, **Target** input
field it runs as Admin - but it creates new taskbar icon not on the same position (e.g. I can't
use `Win+2` to switch to it, instead it creates new tab with new Admin powershell).

Better solution is to use shortcut with target
`"c:\Program Files\ConEmu\ConEmu64.exe" powershell.exe -new_console:a` (ConEmu location with
Chocolatey installation, probably default as well) **and** set **Run as administrator** via
shortcut (advanced) settings.

TODO: How to script this?

## Setting PATH and other environment variables permanently

SETX is the command that should handle it, `/M` tells it to use system environment, not local one.
```
SETX /M JAVA_HOME "c:\Program Files\Java\jdk1.8.0_92"
```

TODO: Is it possible ot use other variable in PATH? How to display unexpanded variable string?


## Problem: Windows 10 and sticky corners on dual monitor

Could have been solved in Windows 8.1 with registry trick, not anymore. Microsoft screwed big time.


## Problem: Blurry fonts on dual monitor

Set both monitors to the same size of font (typically it is 125% on the notebook and 100% on
external monitor, 125% is rather too much for the monitor, so 100% is better for both).


## Git Bash Here in Total Commander

Based on [my blog](https://virgo47.wordpress.com/2013/05/05/git-bash-here-in-console2-in-total-commander-with-keyboard-shortcut-hotkey/)
where it is for Console2 - this time for ConEmu. Setup for user command in Total Commander is (as
found in `AppData\Roaming\GHISLER\usercmd.ini`):

```
[em_git_bash_here]
button=C:\Program Files\Git\git-bash.exe
cmd=""C:\Program Files\ConEmu\ConEmu64.exe""
param=-run "C:\Program Files\Git\git-bash.exe" --no-cd --command=usr/bin/bash.exe -l -i
menu=Git Bash Here
```

`-run` is important otherwise every space separated parameter would be interpreted as a new
console and using quotes around everything wouldn't work either. Then also add this to `wincmd.ini`
in the same directory like `usercmd.ini` (both `Alt+B` and `Ctrl+B` launch Git Bash):

```
[Shortcuts]
C+B=em_git_bash_here
A+B=em_git_bash_here
```