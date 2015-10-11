Java Simon - Simple Monitors for Java
=====================================
[![Build Status](https://travis-ci.org/virgo47/javasimon.svg?branch=master)](https://travis-ci.org/virgo47/javasimon)
[![Coverage Status](https://img.shields.io/coveralls/virgo47/javasimon.svg)](https://coveralls.io/r/virgo47/javasimon)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.javasimon/javasimon-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.javasimon/javasimon-parent)
[![License](http://img.shields.io/:license-New BSD-blue.svg)](https://github.com/virgo47/javasimon/blob/master/license.txt)

Java Simon is a simple monitoring API that allows you to follow and better understand your application.
Monitors (familiarly called Simons) are placed directly into your code and you can choose whether you
want to count something or measure time/duration.

* Current version: 4.1.1 (requires Java 7) [Javadoc](http://virgo47.github.io/javasimon/api/4.1/)
* Version for Java 6: 3.5.2 (October 2014) [Javadoc](http://virgo47.github.io/javasimon/api/3.5/)
* [History of releases](docs/History.md)
* License: [New BSD License](license.txt)

## Getting started

Easiest way to start with Java Simon is to [add Maven dependencies into your Maven project](docs/Maven.md).

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
* Javadoc API: http://virgo47.github.io/javasimon/api/4.0/
* Ohloh page: http://www.ohloh.net/p/javasimon
* Former project page on Google Code: http://code.google.com/p/javasimon/

## Why Java Simon?

Back in 2008 we wanted to use something like JAMon for our products originally, but we lacked two important features:

* better way (or any way for that matter) to organize all those monitors;
* nanosecond resolution.

Simon API gives you **better control** over all those monitors in your big - possibly Java EE - application.
Simons are **organized in a hierarchy** similar to what you can see in virtually any logging API.

Simons **can be disabled** which minimizes their overhead influencing your application. These operations
can be performed on the whole subtrees of Simons which makes partial application monitoring easier.
See [Simon Hierarchy](docs/SimonHierarchy.md) for more.

**Simon measures times in nanos** - and since 2008 nanos have been getting better with every OS and JDK release.
Believe it or not it can make the difference on current very fast machines. See `SystemTimersGranularity` page for more.

## Wanna contribute?

OK, this is the tricky one. Over the years the project got bigger and there
are even parts I don't know properly. That's why I decided that I'll downsize
the core project and leave only core, jdbc, javaee and spring in it. Console
and demo application will be developed as separate companion projects (but
will be retained in the project as-are until that happens).

But feel free to fork, suggest your patches, file an issues, etc. Please,
check our [Development guide](docs/Development.md).

## Wanna tell us something?

So tell us! Visit our [Google Group](http://groups.google.com/group/javasimon)
or file an issue here on GitHub, whatever. We can't promise to fulfill all your dreams
but we want to produce the library YOU like (and so do we - of course ;-)). So if you know
how to make Simon better, without making it something it is not, let us know! We want to know.

## Java Simon name

**Java Simon** is the official name of the project with _Simple Monitoring API_ as a subtitle.
Codename of the project is **javasimon**. We use word Simon as a synonym for a "monitor" in javadoc or on our wiki
- of course we use it only for monitors based on the API. We write Simon mostly with capital S, Java Simon
with space and javasimon as a one word with all lowercase. Word javasimon is probably best to use in search engines.