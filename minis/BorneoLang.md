Notes about imaginary "Java really 2"


## Generic ideas or questions

### Language features

* No language-level serialization - Brian Goetz could go on about this forever
* Operator overloading for sure.
* "Code-literals", not just class, but also methods, fields, anything. This can help with some
problems currently solved by reflection, meta-programming, things like JPA where you need to
generate metadata (now you'd have it) or anywhere you now use attribute paths as strings.
* Meta-data from annotation - imagine Querydsl doesn't need to generate anything and just use
existing entity. That would probably also require extension mechanism.
* Extension methods.
* Lambda, method reference (synergy with code-literals), ... generally Java 8 functional stuff.
* Pattern matching (Scala like, what has Haskell in addition to that?)
* How to treat null?
* var/val
* strongly typed with type inference
* Objects with identity vs Values (no primitives needed).
* Annotations where any parameter can be marked as default (e.g. parameter with name "name" can
be default and used without explicit "name =").
* Method/function default parameters? I'd go for it (trailing).
* More control over package visibility (like sub-packages are not the same as outside packages).

### Language - questionable and/or radical

* Generators - how do ES6 function*/yield relate to Scala sequence comprehension? Resumable return
seems quite messy to me. Also see [Python vs Scala](http://stackoverflow.com/questions/2137619/scala-equivalent-to-python-generators).
* [Crockford on ES6](http://bdadam.com/blog/video-douglas-crockford-about-the-new-good-parts.html):
what about classes? Do we really need/want them? He doesn't like generators (my feeling exactly).
* Crockford also mentions [DEC64](http://dec64.com/) - do we want it, and if do we need it as
built-in type.
* Desctructuring looks good, should work fine with tuples too.
* Is there a nice syntax for multiple ... parameters? This is not possible with Java, but how can
we make it nicer? Is collection literal enough? (Probably should be if it's short enough.)

### Concurrency

* Concurrency? Definitely not with every object being a monitor? Memory model?
* Thread attributes? Like Takipi is recommending setting names, but why not have richer map-like
API? It can still be cleared at start of the request. Is there other good implicit solution for
this? Thread-local vars are not the same, as we have to somehow reach them and they actuall can
leak memory easier than thread attributes (at least I think so).

### Runtime

* What runtime? No chance to make own VM... How to provide "Unsafe"-like things?
* Memory management? JVM-like? Go? With or without GC (like Rust)?
* Prefer memory collocation (value objects should help).

### Libraries

* Modern collection utils, mutable/immutable with literals - should literals be on language level?
can we make the lang so that it just flows even without built-in literals? (Operator overloads?)
* How would it implement regex matching without really building it into language. Again - operator
overload - can we start with some funky operator (like / or ~) that would use some predefined
utility function? Can these be some default extending methods? Can this be redefined/overriden
(e.g. using better regex library)?

### Tools

* Scripting capabilities => REPL, shell.
* In source documentation similar to Javadoc, but in more convenient markup - actually markdown.
Processing should be switchable anyway (ideally using some project meta-data facility) and
supported by IDEs. Default markdown format should be extensible.
* Built-in tools for formatting? Project meta-data definition (e.g. what kind of source-doc format
is used). 

### Resources

* Brian Goetz, [Move Deliberately and Don’t Break Anything](https://youtu.be/K__bKr5mGXY)

## Why not Scala?

* Functionally very good already.
* Meta/code-literals not available(?)


## Examples

### Factorial

### Fibonacci

### JPA :-)
