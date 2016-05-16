Notes about imaginary "Java really 2"


## Generic ideas or questions

* No language-level serialization - Brian Goetz could go on about this forever
* Operator overloading for sure.
* "Code-literals", not just class, but also methods, fields, anything. This can help with some
problems currently solved by reflection, meta-programming, things like JPA where you need to
generate metadata (now you'd have it) or anywhere you now use attribute paths as strings.
* Meta-data from annotation - imagine Querydsl doesn't need to generate anything and just use
existing entity. That would probably also require extension mechanism.
* Extension methods.
* Modern collection utils, mutable/immutable with literals - should literals be on language level?
can we make the lang so that it just flows even without built-in literals? (Operator overloads?)
* How would it implement regex matching without really building it into language. Again - operator
overload - can we start with some funky operator (like / or ~) that would use some predefined
utility function? Can these be some default extending methods? Can this be redefined/overriden
(e.g. using better regex library)?
* Lambda, method reference (synergy with code-literals), ... generally Java 8 functional stuff.
* Pattern matching.
* Scripting capabilities => REPL, shell.
* How to treat null?
* var/val
* strongly typed with type inference
* Memory management? JVM-like? Go? With or without GC (like Rust)?
* What runtime? No chance to make own VM... How to provide "Unsafe"-like things?
* Concurrency? Definitely not with every object being a monitor?
* Thread attributes? Like Takipi is recommending setting names, but why not have richer map-like
API? It can still be cleared at start of the request. Is there other good implicit solution for
this? Thread-local vars are not the same, as we have to somehow reach them and they actuall can
leak memory easier than thread attributes (at least I think so).
* Objects with identity vs Values (no primitives needed).
* Memory collocation.
* Annotations where any parameter can be marked as default (e.g. parameter with name "name" can
be default and used without explicit "name =").

Sources:
* Brian Goetz, [Move Deliberately and Don’t Break Anything](https://youtu.be/K__bKr5mGXY)

## Why not Scala?

* Functionally very good already.
* Meta/code-literals not available(?)


## Examples

### Factorial

### Fibonacci

### JPA :-)
