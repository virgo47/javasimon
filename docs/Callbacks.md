# How to use Callbacks to extend the JavaSimon behavior

## Introduction

Callbacks are addition in version 2 of the Java Simon and they allow you to
add your own hooks to various events. Technically Callbacks are added to Simon
Manager, but hook methods are always called with all necessary parameters to
determine context of the event. They can be structured in a tree, events are
distributed to all installed callbacks, but you can also filter some events
for a callback subtree on the basis of Simon pattern and/or values just
measured, etc.

## Creating your own callback

Because implementing the whole `org.javasimon.callback.Callback` interface
can be annoying, we provide `org.javasimon.callback.CallbackSkeleton` that
implements simple Callback that does nearly nothing (only its `message` and
`warning` methods prints out to stdout/stderr respectively).

Following code snippets are from the class `org.javasimon.examples.CallbackExample`.

## Adding callback to the Manager

We will use default `SimonManager` and we will create callback that just
prints out the information when any `Stopwatch` is started or stopped.
```
	SimonManager.callback().addCallback(new CallbackSkeleton() {
		@Override
		public void onStopwatchStart(Split split) {
			System.out.println("\nStopwatch " + split.getStopwatch().getName() + " has just been started.");
		}

		@Override
		public void onStopwatchStop(Split split, StopwatchSample sample) {
			System.out.println("Stopwatch " + split.getStopwatch().getName()
				+ " has just been stopped (" + SimonUtils.presentNanoTime(split.runningFor()) + ").");
		}
	});
```

Now the rest of the example just starts and stops some Stopwatch.
In the quickest fashion:
```
	SimonManager.getStopwatch(SimonUtils.generateName(null, false)).start().stop();
```

Result should be similar to:
```
Stopwatch org.javasimon.examples.CallbackExample has just been started.
Stopwatch org.javasimon.examples.CallbackExample has just been stopped (833 us).
```

Check out the `org.javasimon.callback.Callback` and `CallbackSkeleton` too to
find out which events are supported.

## Composite and Filter Callbacks

In theory, all your behavior extensions can be written with a single Callback
that is customized for every application. On the other hand it's more practical
to use smaller Callbacks that are written to do simple things - to log events
or send JMX notifications for instance. That's why you probably want to add
more Callbacks for the manager and also to filter events for some of them.

### Composite Callback

Composite behavior is introduced in `org.javasimon.callback.CompositeCallback`
interface that extends from `Callback`. Class `CompositeCallbackImpl` is
default implementation of the Composite Callback and simply delegates all
the events to all child Callbacks.

Composite Callback also sits in every Manager, so when you need more
Callbacks, you can use code like this:
```
	SimonManager.callback().addCallback(new org.javasimon.utils.LoggingCallback());
	SimonManager.callback().addCallback(new ...); // some other callback
...
	// to remove some specific callback
	SimonManager.callback().removeCallback(callback);
```

With `CompositeCallbackImpl` it is safe to remove callbacks while you iterate
them. To remove all the Callbacks one can use this method:
```
	SimonManager.callback().removeAllCallbacks();
```

### Filter Callback

Interface `FilterCallback` introduces method that allows to configure the
filter with a few classes related to this task (`FilterCallback.Rule` and
`FilterCallback.Rule.Type`). There is only one implementation of this Filter -
`CompositeFilterCallback`. This class allows you to filter events that are sent
to all Callback's children - otherwise it is Composite Callback.

Check `org.javasimon.examples.CallbackFilteringExample` for programmatic
Filter Callback example. You can filter events on the basis of Simon name
(simple patterns are allowed, check Javadoc for `SimonPattern`) or an
expression that can utilize values like split time, max/min of Simons, etc.
You can combine any number of rules and specify whether the rule has to be
true or false or whether the rule result is enough or next rules should be
consulted as well.

Because true power of this system is more obvious when it's configured
declaratively, we cover all options in [Configuration](Configuration.md) article.

## Initialization and cleanup

Every Callback can use `initialize()` and `cleanup()` methods to perform necessary life-cycle operations. For example, file can be open in `initialize` and closed in `cleanup`. Initialization is called when the Callback is added to Manager or into another initialized composite callback. It is also called when uninitialized parent composite callback is added into the Manager's Callbacks.

Cleanup is called when the Callback is removed from the Manager (or from the Callback tree that is held by it). Simply put - Callback should be initialized when it is set up so it receives events and cleanup is called whenever Callback is taken away from callbacks that receive events. This is all based on the cooperation of the `Manager` and `CompositeCallback` - custom composite/filter Callbacks can cause problems if not implemented properly.

### Example with web application

Imagine you have a web application and you want to measure requests with a servlet filter. (There is a plan to develop some reasonable universal web filter in the Simon later.) When there is a request that takes more than some threshold you want to know what caused the delay. Let's say your application has a few Simons here and there and you're using Simon JDBC proxy driver. Printing every Split into the log is an option, but with many parallel requests it would be difficult task to identify the right log entries - and there are many log events you don't want at all.

What you want is to print all Splits belonging to the particular request AFTER you find out that the request is too long. That means that you have to cumulate Splits for the request somehow and print them only when the request is not to your liking.

To gather all the Splits we will use static thread-local `List` and we will use Callback to fill the list. Variable will be either in the web Filter or in the Callback - depends entirely on your decision (what should depend on what). In the following snippet we will assume it's in the Filter:
```
	public static final ThreadLocal<List<Split>> SPLITS = new ThreadLocal<List<Split>>();

	...code in filter method
	SPLITS.set(new ArrayList<Split>());
	Split reqSplit = SimonManager.getStopwatch(null).start(); // anonymous
	chain.doFilter(request, wrapper);
	reqSplit.stop();
	if (reqSplit.runningFor() > TOO_MUCH) {
		logSomehow(SPLITS.get());
	}
	SPLITS.remove();
```

And now the Callback - let's add this code to servlet filter `init` method:
```
	SimonManager.callback().addCallback(new CallbackSkeleton() {
		@Override
		public void onStopwatchStop(Split split, StopwatchSample sample) {
List<Split> list = MyWebFilter.SPLITS.get();
			if (list != null) {
				list.add(split);
			}
		}
	});
```

As you see, the split is saved only if thread-local list is initialized, otherwise the Callback does nothing. Few lines of code, powerful solution beyond Simon's out-of-the-box skills - all thanks to Callback.

### Java SE alternative example

You can check out another Callback cumulating splits in our examples: http://code.google.com/p/javasimon/source/browse/trunk/examples/src/main/java/org/javasimon/testapp/SplitCumulatorCallback.java

This one has all the start/stop of the cumulation process inside of it and it's controlled by start/stop of an arbitrary Stopwatch - you can configure the name of this "controller" Stopwatch. This example is used in Java SE environment and it's configured using the property on command line:
```
java -Djavasimon.config.file=examples/testapp-config.xml ...classpath... class-name
```

The config file is here: http://code.google.com/p/javasimon/source/browse/trunk/examples/testapp-config.xml

And finally the code that is measured and that triggers the cumulation in the Callback:
http://code.google.com/p/javasimon/source/browse/trunk/examples/src/main/java/org/javasimon/testapp/test/Runner.java

This Callback lacks the feature "print only if the controller Stopwatch take more than specified time", but you can easily add another configuration setter and another if condition to stop event handler.

BTW: You can use configuration also in Java EE environment - add lines into the MyWebFilter `init`:
```
SimonManager.configuration().readConfig(...reader for resource or file);
```

The configuration is added to existing Manager's configuration, which means that all Callbacks mentioned in the configuration resource are added even if there are the same ones already.

### Static considerations

If you use default `SimonManager` in Java EE, consider doing some cleanup. For instance - in our web filter it might be nice to remove installed Callback in filter's `destroy` method, or at least check if the Callback isn't already there in the `init`. While application can be redeployed many times (typically during the development) `SimonManager` still holds all its Callbacks.

Removing them altogether - on the other hand - might be unpleasant for other applications in the same container. You can also use your own `Manager`, but currently there is no way how let the JDBC driver to use your Manager instead of the default one (this is inherent problem, that Java EE data sources are independent on the applications). However this is not the topic for this article.

## Conclusion

Callbacks are great way how to extend the functionality provided by the Java
Simon library. If there is something you miss about Simon, think if your code
that can be executed during any of those events handled by Callbacks can't do
the trick. Some of the events are Manager related, but most of them react on
arbitrary Simon (Counter or Stopwatch) operations. There is also flexible way
how to configure Callbacks in a declarative way - see [Configuration] for more.