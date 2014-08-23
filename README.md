# Java Simon - Simple Monitors for Java =

Java Simon is a simple monitoring API that allows you to follow and better understand your application.
Monitors (familiarly called Simons) are placed directly into your code and you can choose whether you
want to count something or measure time/duration.

## Getting started

Easiest way to start with Java Simon is adding Maven dependencies into your Maven project.
See http://code.google.com/p/javasimon/wiki/MavenSupport for more.

A monitors in Java Simon is called "Simon". There are two types of Simons available: `Counter` and `Stopwatch`.
Counter tracks single long value, its maximum and minimum. Stopwatch measures time and tracks
number of measurements (splits), total time, split minimum and maximum, etc.

### Simon Manager

You obtain Simons from the `SimonManager`:

```
Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.examples.HelloWorld-stopwatch");
```

Here we obtained stopwatch Simon. If the Simon is accessed first time it is created. If you access existing Simon,
type of the Simon must match - you can't create counter with the same name (unless you destroy the Simon first).

### Stopwatch

Using stopwatch is simple:

```
Split split = stopwatch.start(); // returns split object
// here goes the measured code
long time = split.stop(); // returns the split time in ns
```

After few runs of your measured code you can get additional information from stopwatch:

```
long totalNanos = stopwatch.getTotal();
long maxSplit = stopwatch.getMax();
long minSplit = stopwatch.getMin();
```

You can use convenient utility to print the results (note ns/us/ms/s unit after the number):

```
System.out.println("Total time: " + SimonUtils.presentNanoTime(totalNanos));
```

Or simply print the Simon itself, it has nice `toString` output.

For more check other Resources (lower), or longer [Getting Started](docs/GettingStarted.md),
or our Javadoc, that actually contains something on the overview page (under package list).

## Resources

Project is [hosted on GitHub](https://github.com/virgo47/javasimon/), these are related pages: 

* Our homepage on Google+: http://www.javasimon.org or https://plus.google.com/b/115141838919870730025/115141838919870730025
* Javadoc API: http://javasimon.googlecode.com/svn/javadoc/api-3.5/index.html
* Ohloh page: http://www.ohloh.net/p/javasimon
* Former project page on Google Code: http://code.google.com/p/javasimon/

## Wanna contribute?

OK, this is the tricky one. Over the years I found that I'm less enthusiastic about Java Simon. Project got bigger,
there are even parts I don't know properly. That's why I decided that I'll downsize the core project and leave
only core, jdbc, javaee and spring in it. Console and demo application will be developed as separate companion
projects.

Of course, feel free to fork, suggest your patches, etc. Please, check our [Development guide](docs/Development.md).

## Java Simon name

**Java Simon** is the official name of the project with _Simple Monitoring API_ as a subtitle.
Codename of the project is **javasimon**. We use word Simon as a synonym for a "monitor" in javadoc or on our wiki
- of course we use it only for monitors based on the API. We write Simon mostly with capital S, Java Simon
with space and javasimon as a one word with all lowercase. Word javasimon is probably best to use in search engines.