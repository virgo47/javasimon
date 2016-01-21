# How to create virtual network with VirtualBox

Let's say I want to create mini-network with two computers:
* Windows Server 2012 acting as a domain controller,
* Windows 10 as a member of domain.

We need to choose the right way of networking from: https://www.virtualbox.org/manual/ch06.html

## Internal networking

Very easy, for both machines select in VBox **Settings/Network**, tab **Adapter 1** networking type
(**Attached to**) of **Internal Network** and choose the same name, e.g. `testdomain`.
 
Start both hosts and use `ipconfig` to find their IPs and try to ping them. Out of box I could
ping Windows 10 from the other, not the other way around, because Windows Server 2012 has disabled
inbound ping. Follow [this](http://blog.blksthl.com/2012/11/20/how-to-enable-ping-in-windows-server-2012/)
to fix the problem.