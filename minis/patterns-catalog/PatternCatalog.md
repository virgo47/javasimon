**Command Query Responsibility Segregation (CQRS)**, architectural pattern,
(on Wikipedia since 2003, Bertrand Mayer, Eiffel, probably much older),
http://martinfowler.com/bliki/CQRS.html,
https://en.wikipedia.org/wiki/Command%E2%80%93query_separation,
https://msdn.microsoft.com/en-us/library/dn568103.aspx?f=255&MSPPError=-2147217396

**Saga pattern**, architectural pattern, 1987.
"...a distribution of multiple workflows across multiple systems, each providing a path (fork) of
compensating actions in the event that any of the steps in the workflow fails."
[Sagas](http://www.cs.cornell.edu/andru/cs711/2002fa/reading/sagas.pdf) (1987),
[ACTA: The SAGA Continues](http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.43.6829) (1992),
[Clarifying the Saga pattern](http://kellabyte.com/2012/05/30/clarifying-the-saga-pattern/) (2012)

**State-Action-Model (SAM)**, architectural pattern, 2015,
http://www.ebpml.org/blog15/2015/06/sam-the-state-action-model-pattern/,
http://www.infoq.com/articles/no-more-mvc-frameworks

**Strategy**, design pattern (GoF), 1994 and sooner. Strategy allows one of a family of algorithms
to be selected on-the-fly at runtime. https://en.wikipedia.org/wiki/Strategy_pattern,
Examples: Java `Comparator`, 



# From https://en.wikipedia.org/wiki/Design_Patterns

Creational
Main article: Creational pattern

Creational patterns are ones that create objects for you, rather than having you instantiate objects directly. This gives your program more flexibility in deciding which objects need to be created for a given case.

    Abstract factory pattern groups object factories that have a common theme.
    Builder pattern constructs complex objects by separating construction and representation.
    Factory method pattern creates objects without specifying the exact class to create.
    Prototype pattern creates objects by cloning an existing object.
    Singleton pattern restricts object creation for a class to only one instance.

Structural

These concern class and object composition. They use inheritance to compose interfaces and define ways to compose objects to obtain new functionality.

    Adapter allows classes with incompatible interfaces to work together by wrapping its own interface around that of an already existing class.
    Bridge decouples an abstraction from its implementation so that the two can vary independently.
    Composite composes zero-or-more similar objects so that they can be manipulated as one object.
    Decorator dynamically adds/overrides behaviour in an existing method of an object.
    Facade provides a simplified interface to a large body of code.
    Flyweight reduces the cost of creating and manipulating a large number of similar objects.
    Proxy provides a placeholder for another object to control access, reduce cost, and reduce complexity.

Behavioral

Most of these design patterns are specifically concerned with communication between objects.

    Chain of responsibility delegates commands to a chain of processing objects.
    Command creates objects which encapsulate actions and parameters.
    Interpreter implements a specialized language.
    Iterator accesses the elements of an object sequentially without exposing its underlying representation.
    Mediator allows loose coupling between classes by being the only class that has detailed knowledge of their methods.
    Memento provides the ability to restore an object to its previous state (undo).
    Observer is a publish/subscribe pattern which allows a number of observer objects to see an event.
    State allows an object to alter its behavior when its internal state changes.
    
    Template method defines the skeleton of an algorithm as an abstract class, allowing its subclasses to provide concrete behavior.
    Visitor separates an algorithm from an object structure by moving the hierarchy of methods into one object.
