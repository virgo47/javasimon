## Simon Hierarchy

While Simons do just fine on their own (measuring) they are organized in the tree hierarchy because
of better manageability. You can enable/disable whole sub-trees (also via JMX) or you can perform various
aggregative operations on them. You can organize monitors on your own as well (e.g. groups) - anyway this
tree hierarchy is always there for you.

## Simon's place in the hierarchy

Name of the Simon is always fully qualified and it always position Simon into the tree somewhere.
Name is structured the same way like name of the Logger from Java Util Logging API. Dot (.) works
as a separator of name components and as you read the name from left to right you go down the hierarchy
to your Simon. Generally you always work with Simon via its full name, local name is rarely useful.

Obtaining the Simon is fast - it's done via concurrent hash-map. Only the Simon creation is a bit more expansive
we have to place the monitor into the tree, find out its effective state (if it's enabled), etc.
Basic usage of Simons is not affected by the overhead of working with the tree. If you perform hierarchical
data sampling (aggregations) or if you manage your Simons (disable, enable) then you work with the tree structure.

## Simon state

Simon can be enabled or disabled - this determines if it does its work (measures, counts) or not.
Disabled Simon affects the application as little as possible. We call this **effective state**
and it can be only enabled or disabled. However there is also an "inner" state of the Simon (simply **state**)
and it can be enabled, disabled or **inherit** (see enumeration `SimonState`). If the state is set to inherit,
tree is traversed parent by parent seeking for the first Simon with disabled or enabled state.
This determines the effective state of the Simon. Or you can simply say that Simon inherits
effective state of the parent - it's still the same.

Because effective state is used a lot by the Simon, it's always stored in every Simon - so don't worry
about the performance. :-)

## Nodes in between?

When you create stopwatch with name `com.myproject.package.something.monitor1` the stopwatch object
is created - but it has to have parent in order to be in the tree. Its parent has name
`com.myproject.package.something` and it has unspecified type. This is implemented by `UnknownSimon` class
and you can replace this type of Simon with the monitor of your choice later. If you destroy monitor with
some children, it's not actually removed - it's replaced by the "unknown" Simon. This node is needed
to provide necessary hierarchy-related functionality.