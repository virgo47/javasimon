# How to create virtual network with VirtualBox

Let's say I want to create mini-network with two computers:
* Windows Server 2012 acting as a domain controller,
* Windows 10 as a member of domain.

We need to choose the right way of networking from: https://www.virtualbox.org/manual/ch06.html

## Internal networking

Very easy, for both machines select in VBox **Settings/Network**, tab **Adapter 1** networking type
(**Attached to**) of **Internal Network** and choose the same name, e.g. `testdomain`. In this case
host is not accessible from the guests.
 
Start both machines and use `ipconfig` to find their IPs and try to ping them. Out of box I could
ping Windows 10 from the other, not the other way around, because Windows Server 2012 has disabled
inbound ping. Follow [this](http://blog.blksthl.com/2012/11/20/how-to-enable-ping-in-windows-server-2012/)
to fix the problem.

## Host-only Adapter
 
Works similar like Internal networking, but host and guests are on the same network. Using
`ipconfig` on the host we can find **Ethernet adapter VirtualBox Host-Only Network** and see its
IP. Name of the adapter can be different, there can be multiple adapters as well -- this can be
set in **File/Preferences/Network**, tab **Host-only Networks**.

## Outside connectivity

If we want to leave NAT networking so the machines are not cut from the outside world (handy
for browsing, any downloads, updates, etc.) we may leave **Adapter 1** as is and set-up Internal
Network (or Host-only Adapter) on **Adapter 2** (or vice versa).