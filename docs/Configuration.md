# Java Simon Configuration

*To be changed in future!* Current configuration system is used rarely, if you
configure Simon this way, please let us know how and for what purpose, so a new
system can suit your needs. (Add an issue or write to virgo47@gmail.com.)

## Introduction

Java Simon configuration facility allows you to configure Java Simon usage
in a declarative manner. While originally it should cover Simon configurations,
it is now used mostly to configure [Callbacks](Callbacks.md). Configuration is always
`Manager` related, various Managers have independent configurations.

## How to use it?

Your configuration has to be stored in a file (or a resource) and you can load
it either programmatically or you can specify it on the command line (JVM
property).

### Programmatically

For instance - in code you can load it this way:
```
	Manager manager = new EnabledManager();
...
	FileReader reader = new FileReader("my-config.xml");
	manager.configuration().readConfig(reader);
	reader.close();
```

You have to close the provided reader by yourself. Previous configuration is
preserved (although overwritten if something collides). You can use this way
to read configuration for any Manager - this is not possible for command line
options described later.

While you have to add a few lines of code to perform the configuration, it's
still faster than doing the whole configuration in the code (for instance
filter-callbacks are "funny" when configured in code, avoid it when you can
;-)). You can also use this in Java EE environment, where JVM properties might
not be an option.

### JVM properties

CLI options (JVM properties) work only for default `SimonManager`. Use this
to read configuration from the file:
```
java -Djavasimon.config.file=my-config.xml ...
```

Or this property to read the configuration from the resource on the classpath:
```
java -Djavasimon.config.resource=com/acme/simon-config.xml ...
```

You can set these properties programmatically as well - before you use
`SimonManager` for the first time, or you can explicitly call
`SimonManager.init()` to re-read configuration. In this case previous
configuration is cleared (but Simons and [Callbacks](Callbacks.md) of the
default manager are not removed or cleared). In case both properties are set,
file is read first, then the resource.

## Configuration format

Configuration is stored in XML format and we'll go through the format step by
step. Basic structure of the file is as follows:
```
<simon-configuration>
	<callback .../>
	<filter-callback .../>
	<simon .../>
</simon-configuration>
```

First two elements add Callback to the Manager, third element sets up Simon or
set of Simons. You can use any number of these elements in any order. While
Simon configuration is now not used too much, Callback configuration is the
major part of the configuration facility.
(See [ManagerConfiguration javadoc](http://virgo47.github.io/docs/javasimon/api/4.1/org/javasimon/ManagerConfiguration.html))

### Simon setup

Simon setup was originally used to specify stat-processor (that is now left
behind in version 2), to enforce type of the Simon (also gone :-)) or to setup
state of the Simon upon its creation. Element `<simon>` has mandatory attribute
`pattern` that determines which Simon(s) will be affected. The only other
usable attribute is `state` with possible values `enable`, `disable` or
`inherit` (casing is ignored for these values).

Example - every Simon created with the name starting with `org.javasimon.test.`
will be disabled:
```
	<simon pattern="org.javasimon.test.*" state="disabled"/>
```

Order of `simon` elements is important as the later overrule the former ones.

This part of the configuration is rather questionable and may be changed in the
future. Loading of the config-file does NOT affect state of existing Simons -
this is also questionable, as you can't use config files for batch state
changes. Functions of this element are open to discussion.
(Javadoc for [SimonPattern](http://virgo47.github.io/docs/javasimon/api/4.1/org/javasimon/SimonPattern.html))

### Callbacks

Element `callback` has one optional attribute `class` and you can use three
nested elements - `set` and then `callback` or `filter-callback` - like under
the top element. All `set` elements must be first, than you can name your
callbacks in any order whether filter or not.

Sets are used to configure the callback via setters. Name of the setter is
derived from the mandatory `property` attribute - first character is
capitalized and prefix `set` is added (kinda standard ;-)). Setter type MUST
be String - any necessary conversions must be done within the setter method.
Alternatively you can use setter without value, in this case setter without
parameter is called. This can be used to switch-like behavior.

Example - setting JDBC logging Callback that logs JDBC proxy driver events:
```
	<callback class="org.javasimon.jdbc.logging.LoggingCallback">
		<set property="prefix" value="org.javasimon.testapp.jdbc"/>
		<set property="loggerName" value="myapp.testdb"/>
		<set property="logFilename" value="myapp.log"/>
 		<set property="logToConsole"/>
		<set property="logFormat" value="csv"/>
	</callback>
```

Aside to `set` element, you can nest other `callback` and `filter-callback`
elements if the configured callback is composite. If no `class` attribute is
used then the `CompositeCallback` will be created.

Unless you create your own composite callback you'll not mix set and callbacks
elements because default composite callback doesn't have properties to set and
normal callbacks mostly doesn't support composite behavior. The same applies
to filter callbacks.

### Filter Callbacks

As with normal `callback`, this one has optional attribute `class` too - if it
is not provided then the `CompositeFilterCallback` will be created. This
default implementation does not have any setters, but your own implementations
can have some. All nested elements mentioned for normal Callback work here as
well (if they are meaningful) plus you can configure the filter with `rule`
elements - these must be first.

Let's start with an example:
```
	<filter-callback>
		<rule type="must" pattern="org.javasimon.test.good.*"/>
		<rule event="stop">
			<condition>
				<![CDATA[
						split > 5000 || split < 10000
					]]>
			</condition>
		</rule>

		<callback class="org.javasimon.utils.LoggingCallback">
			<set property="logger" value="org.javasimon.test"/>
			<set property="level" value="INFO"/>
		</callback>
	</filter-callback>
```

Element `rule` has no mandatory attribute. Attribute `type` affects how rule is
evaluated - it has three possible values `must`, `suffice` and `must-not`
(casing is ignored). `Must` means that the rule must be satisfied and the next
rule is evaluated (if there is any), `suffice` means that if the rule is
satisfied filter passes the event, otherwise it consults following rules - and
finally `must-not` means that the rule result must be false and following rules
are consulted. Rule order is obviously important (thanks to `suffice` option).

#### Events

Attribute `event` enumerates Callback events that this rule applies to. You can
name any events, separate them with comma. Valid names of events are:

  * *reset* - Simon reset;
  * *stopwatch-start* - stopwatch is started;
  * *stopwatch-stop* - stopwatch (split) is stopped;
  * *stopwatch-add* - value (split) is added to stopwatch via `addTime` method;
  * *counter-increase* - counter is increased (both methods);
  * *counter-decrease* - counter is decreased (both methods);
  * *counter-set* - counter is set to a particular value;
  * *created* - Simon is created;
  * *destroyed* - Simon is destroyed;
  * *clear* - Manager is cleared;
  * *message* - arbitrary message event;
  * *warning* - called when some non-fatal error occurs (e.g. configuration syntax error);
  * *all* - meta-action - rule applies to all events.

When rules are consulted one after one, only rules that apply to the called
event are processed.

#### Patterns and Conditions

Finally there are two attributes that really decide if the rule is satisfied
or not - `pattern` and `condition`. See (Javadoc for [SimonPattern](http://virgo47.github.io/docs/javasimon/api/4.1/org/javasimon/SimonPattern.html))
for the format of the pattern.

Condition allows you to use various values from the event context and use
them to determine if the rule passes or not. Variables that can be used are:
  * *split* - last split time in ns;
  * *active* - currently active splits of the Stopwatch;
  * *maxactive* - maximal value of active splits of the Stopwatch;
  * *counter* - counter value of the Counter/Stopwatch;
  * *max* - max value of the Counter/Stopwatch;
  * *min* - min value of the Counter/Stopwatch;
  * *total* - total time of the Stopwatch;
  * *value* - currently incremented/decremented/set value for the Counter;

I'm sure the context where you can use these values is intuitive, you hardly
can expect that `split` will have any value for event `decrement`.

#### Condition evaluation

Conditions are evaluated using built-in "ecmascript" script engine, that is
Rhino or Nashorn on Oracle JDK. If no "ecmascript" script engine is found or
provided, this facility cannot be used.

Conditions must eval to boolean value, e.g. "1" or "split" condition will fail
during initialization (conditions are checked and executed with dummy values
when created).

In addition to pure ECMAScript (JavaScript) expressions there are some
extensions possible for easier XML support (avoiding entities) and
human readable time values.

Following expressions/strings are supported and replaced as follows
(quoted because spaces matter):
  * " lt " is replaced by " < ",
  * " le " is replaced by " <= ",
  * " eq " is replaced by " == ",
  * " ne " is replaced by " != ",
  * " gt " is replaced by " > ",
  * " ge " is replaced by " >= ",
  * " and " is replaced by " && ",
  * " or " is replaced by " || ",
  * " not " is replaced by " ! ",
  * "(\d)s" is replaced by "$1000000000" (that is any number followd by s (second) is replaced by the same number followd by 9 zeros (billion)),
  * "(\d)ms" is replaced by "$1000000" (the same for ms/million)),
  * "(\d)us" is replaced by "$1000" (the same for us (microsecond)/thousand),

Last three replacements reflect the fact that split times are in nanoseconds.
This allows for expressions like `split > 1s` which is more convenient and readable.

## Conclusion

Simon configuration facility is now used mostly to set up Callbacks - and
mostly in Java SE applications via JVM properties. You can use it in Java EE
environment as well, but you have to do it programmatically - on the other
hand, you are not limited to default `SimonManager` in that case. Maybe in
the future the configuration will be richer and will be also more Simon
related - but this depends also on public demand. :-)