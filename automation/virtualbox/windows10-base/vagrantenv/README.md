Copy content of this directory to your Vagrant environment directory (directory where you
run `vagrant init/up` commands) so they are shared in `C:\Vagrant` and then simply run
the `customize.ps1` script from within the host.

You may also copy other resources that cannot be checked-in, like `wincmd.key` (license for
Total Commander), etc.

After running the script, logout/logon is required for some changes to take effect.
TODO: Or should I just restart some programs like classic shell and file explorer?