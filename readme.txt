= Java Simon - Simple Monitors for Java =

This software is distributed under the terms of the FSF Lesser Gnu Public License:
  * check "lgpl.txt" in the root directory of the project
  * or check it online http://www.gnu.org/licenses/lgpl.html

Check this readme online for updates: http://code.google.com/p/javasimon/wiki/Readme

== BUILD ==

  * Simon depends on JDK only (at this moment), you need JDK 1.5 or higher.
  * Use "ant" to build the Simon, find the JAR file under the "build" directory.
  * Some examples requires other libraries (h2, JAMon, ...). If you checkout the project, you have to download them and put them to classpath by yourself. If you download our zip (when available), they are included.

== USAGE ==

There are two types of Simons available: Counter and Stopwatch. Counter tracks single long value, its maximum and minimum. Stopwatch measures time and tracks number of measurements (splits), total time, split minimum and maximum. If you thinkg it's not enough you can get more statistic information by adding StatProcessor implementation to your Simon.

=== Factory ===

You obtain Simons from factory:
{{{
Stopwatch stopwatch = SimonFactory.getStopwatch("org.javasimon.examples.HelloWorld-stopwatch");
}}}

Here we obtained stopwatch Simon. If the Simon is accessed first time it is created. If you access existing Simon, type of the Simon must match - you can't create counter with the same name (unless you destroy the Simon first).

=== Stopwatch ===

Using stopwatch is simple:
{{{
stopwatch.start(); // returns this, so you can call it right after getStopwatch()
// here goes measured code
long time = stopwatch.stop(); // returns the time
}}}

After few runs of your measured code you can get additional information from stopwatch:
{{{
long totalNanos = stopwatch.getTotal();
long maxSplit = stopwatch.getMax();
long minSplit = stopwatch.getMin();
}}}

You can use convenient utility to print the results (note ns/us/ms/s unit after the number:
{{{
System.out.println("Total time: " + SimonUtils.presentNanoTime(totalNanos));
}}}

Or simply print the Simon itself, it has nice `toString` output.

== RESOURCES ==

Project is hosted on Google Code as "javasimon":
  * Homepage: http://code.google.com/p/javasimon/
  * Download: http://code.google.com/p/javasimon/downloads/list
  * Source browser: http://code.google.com/p/javasimon/source/browse/
  * Issue tracker: http://code.google.com/p/javasimon/issues/list

Project tests use TestNG library:
  * http://testng.org/

Project example uses these libraries (you don't need them to use JavaSimon):
  * JAMon API: http://jamonapi.sourceforge.net/
  * H2 Database: http://www.h2database.com/
