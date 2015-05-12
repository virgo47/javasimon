# Getting started

The easiest way how to start playing with Java Simon is to
[add the Maven dependency](Maven.md). If you're not using Maven or something
that can use Maven Central repository you'll have to download the JARs from
there manually. If you want to build from the sources, you can either checkout
from GitHub or just use the "Donwload ZIP" button at the bottom of the right
bar. Simon can be built by Maven 3.x (still official build for Simon) or
Gradle (experimental, but working).

## What is Simon?

We refer to the library as to *Java Simon* which is official name of the
project. We use _Java Simon API_ or _Simon API_ too or you can use the codename
of the project **javasimon** which is also the best word to search for on the
Internet. All these terms refer to the library itself.

If we use word _Simon_ alone (without API addition) we refer to monitor
implemented by the API. We talk about Simons whenever type (stopwatch or
counter) of the monitor is not important. Simons are managed by the Simon
Manager (class [`org.javasimon.Manager`](../core/src/main/java/org/javasimon/Manager.java)
or convenient static version [`org.javasimon.SimonManager`](../core/src/main/java/org/javasimon/SimonManager.java)).
Manager provides access to any Simon by its name, you can obtain list of all
Simons or you can enable or disable the whole API.

So the Simon is monitor that allows you to measure some aspect of your
application. For example – you use `Stopwatch` to measure time or `Counter`
to track some important number. Before we dig more into Simons in theory we
want to show you some code as an example.

## Let's measure something!

Sure, it is time to see some real code now. We will try to quickly cover a few
features Java Simon provides without going into details.

### Hello world

If you want to check something very simple, check `HelloWorld` example (class
[`org.javasimon.examples.HelloWorld`](../examples/src/main/java/org/javasimon/examples/HelloWorld.java)).
It is the simplest time measurement possible and hardly realistic. But it shows
Simon in action! Main method of this example goes like this:
```
// get us some stopwatch Simon
Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.examples.HelloWorld-stopwatch");

Split split = stopwatch.start(); // start the stopwatch
System.out.println("Hello world, " + stopwatch); // print it
split.stop(); // stop it

System.out.println("Result: " + stopwatch); // here we print our stopwatch again
```

Output of this code would be:
```
Hello world, Simon Stopwatch: [org.javasimon.examples.HelloWorld-stopwatch INHERIT]
 total 0 ns, counter 0, max 0 ns, min undef
Result: Simon Stopwatch: [org.javasimon.examples.HelloWorld-stopwatch INHERIT]
 total 59.2 ms, counter 1, max 59.2 ms, min 59.2 ms
```

The first line prints the Simon (name is bold) with zero results – that is because stopwatch adds the split time to results after the stop and you cannot see times in progress. Second line shows the results after measurement. As you can see, one line of code – along with one `toString()` of our stopwatch – took 59 ms on our test computer.

### And now for real

Now we start with the simple application that calls `strangeMethod` ten times and we want to know how this method performs:
```
import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;

public final class Example {
	public static void main(String[] args) {
		Stopwatch stopwatch = SimonManager.getStopwatch("stopwatch");
		for (int i = 0; i < 10; i++) {
			strangeMethod();
		}
		System.out.println("Stopwatch: " + stopwatch);
	}

	/**
	 * Method that lasts randomly from ~0 to ~2500 ms.
	 */
	private static void strangeMethod() {
		Split split = SimonManager.getStopwatch("stopwatch").start();
		long random = (long) (Math.random() * 50);
		try {
			Thread.sleep(random * random);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		split.stop();
	}
}
```

The code is easy and you probably obtain output like this:
```
Stopwatch: Simon Stopwatch: [stopwatch INHERIT] total 11.5 s, counter 10, max 2.21 s, min 121 ms
```

As you can see, Simon gives you the most important information, but you may be interested in more – especially if you are fan of statistics. Simon's `toString` output is short for a reason and if you want to obtain all values, you have to obtain so the `Sample` and print it out:
```
	public static void main(String[] args) {
		Stopwatch stopwatch = SimonManager.getStopwatch("stopwatch");

		for (int i = 0; i < 10; i++) {
			strangeMethod();
		}
		System.out.println(stopwatch);
		System.out.println(stopwatch.sample());
	}
```

Recompile and run it again:
```
Simon Stopwatch: [stopwatch INHERIT] total 9.54 s, counter 10, max 2.03 s, min 16.6 ms
StopwatchSample{total=9.54 s, counter=10, min=16.6 ms, max=2.03 s, minTimestamp=090623-102015.866, maxTimestamp=090623-102012.967, active=0, maxActive=1, maxActiveTimestamp=090623-102015.866, last=1.76 s, mean=954 ms, standardDeviation=679 ms, variance=5.1276613691815206E17, varianceN=4.614895232263369E17, note=null}
```

### Watching the application over time

One of the most important goals of Java Simon is to allow you to track application performance over some time to find out how various trends evolve, where application gets slower over time, etc. For this you need to have a mechanisms to collect data periodically and to write them – preferably into some persistent store. Java Simon currently does not have complete solution to do this (but we are working on it) and you have to do it by yourself – but there is some support. Generally you can collect data either remotely (JMX or a custom solution) which allows to do the processing in a different process or even on a different host – or you can use some embedded solution.

We will now implement very simple version of the letter option. It will consist of a thread that periodically dumps values of our stopwatch:
```
static class Sampler extends Thread {
	/**
	 * Method implementing the code of the thread.
	 */
	public void run() {
		while (true) {
			Stopwatch stopwatch = SimonManager.getStopwatch("sampled-stopwatch");
			System.out.println("\nstopwatch = " + stopwatch);
			System.out.println("Stopwatch sample: " + stopwatch.sample());
			// TODO: uncomment this if you want - of course commment the line above
//			System.out.println("Stopwatch sample: " + stopwatch.sampleAndReset());
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
```

The key here is the `sample` method that returns the map of all Simon's values that might be of
any importance – alternatively you can call `sampleAndReset` to always reset the Simon after
sample which means that you get results only for the last period and we do not want to cumulate
them. Let's change the main method to this:
```
	public static void main(String[] args) {
		Stopwatch stopwatch = SimonManager.getStopwatch("stopwatch");

		// Starts the sampler
		Sampler sampler = new Sampler();
		sampler.setDaemon(true);
		sampler.start();

		// Starts the measuring
		while (true) {
			strangeMethod();
		}
	}
```

Recompile, run and you probably get the output like this:
```
Stopwatch sample: StopwatchSample{total=0 ns, counter=0, min=undef, max=0 ns, minTimestamp=700101-010000.000, maxTimestamp=700101-010000.000, active=1, maxActive=1, maxActiveTimestamp=090623-100829.390, last=0 ns, mean=0 ns, standardDeviation=0 ns, variance=0.0, varianceN=0.0, note=null}
Stopwatch sample: StopwatchSample{total=9.79 s, counter=10, min=16.5 ms, max=2.30 s, minTimestamp=090623-100835.435, maxTimestamp=090623-100837.740, active=1, maxActive=1, maxActiveTimestamp=090623-100839.185, last=1.44 s, mean=979 ms, standardDeviation=774 ms, variance=6.6578618931696602E17, varianceN=5.992075703852695E17, note=null}
Stopwatch sample: StopwatchSample{total=18.7 s, counter=25, min=9.71 ms, max=2.30 s, minTimestamp=090623-100841.220, maxTimestamp=090623-100837.740, active=1, maxActive=1, maxActiveTimestamp=090623-100848.064, last=121 ms, mean=747 ms, standardDeviation=738 ms, variance=5.6791014914607597E17, varianceN=5.4519374318023296E17, note=null}
Stopwatch sample: StopwatchSample{total=29.4 s, counter=32, min=9.71 ms, max=2.40 s, minTimestamp=090623-100841.220, maxTimestamp=090623-100856.498, active=1, maxActive=1, maxActiveTimestamp=090623-100858.803, last=2.30 s, mean=919 ms, standardDeviation=824 ms, variance=7.0009560615890086E17, varianceN=6.782176184664352E17, note=null}
```

Press Ctrl+C to stop it (or terminate any other way). As expected, total time values changes
oscillate around10 seconds which is the sample period. Now it is up to you how you process these
values – you can store them into database, dump them into a file or send them via network to some
monitoring solution. Later you might want to analyze them.

## What next?

You may check [various example programs](../examples/src/main/java/org/javasimon/examples)
that show the basics or how this basic functionality can be extended using [callbacks](Callbacks.md).