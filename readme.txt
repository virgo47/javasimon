#summary readme.txt included in the download

= Java Simon - Simple Monitors for Java =

This software is distributed under the terms of the FSF Lesser Gnu Public License:
  * check "lgpl.txt" in the root directory of the project
  * or check it online http://www.gnu.org/licenses/lgpl.html

Check this readme online for updates: http://code.google.com/p/javasimon/wiki/Readme

== Build ==

  * Compiled Simon JAR depends on JDK only (at this moment), you need JDK 1.5 or higher.
  * Use "ant" to build the Simon, find the JAR file under the "build" directory. For build you need JDK 1.5, NOT JDK 1.6.
  * Some examples requires other libraries (h2, JAMon, ...). If you checkout the project, you have to download them and put them to classpath by yourself. If you download our ZIP, they are included.

== Usage ==

There are two types of Simons available: `Counter` and `Stopwatch`. Counter tracks single long value, its maximum and minimum. Stopwatch measures time and tracks number of measurements (splits), total time, split minimum and maximum. If you think it's not enough you can get more statistic information by adding `StatProcessor` implementation to your Simon.

=== Simon Manager ===

You obtain Simons from the `SimonManager`:
{{{
Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.examples.HelloWorld-stopwatch");
}}}

Here we obtained stopwatch Simon. If the Simon is accessed first time it is created. If you access existing Simon, type of the Simon must match - you can't create counter with the same name (unless you destroy the Simon first).

=== Stopwatch ===

Using stopwatch is simple:
{{{
Split split = stopwatch.start();
// here goes measured code
long time = split.stop(); // returns the time
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

== Resources ==

Project is hosted on Google Code as "javasimon":
  * Homepage: http://code.google.com/p/javasimon/
  * Download: http://code.google.com/p/javasimon/downloads/list
  * Source browser: http://code.google.com/p/javasimon/source/browse/
  * Issue tracker: http://code.google.com/p/javasimon/issues/list

Project tests use TestNG library:
  * http://testng.org/

Project example uses these libraries (you don't need them to use Java Simon):
  * JAMon API: http://jamonapi.sourceforge.net/
  * H2 Database: http://www.h2database.com/

Spring integration support requires following libraries:
  * AOP Alliance JAR: http://sourceforge.net/projects/aopalliance
  * Spring core+aop JARs: http://www.springsource.org/
  * Commons Logging: http://commons.apache.org/logging/

== Java Simon name ==

*Java Simon* is the official name of the project with _Simple Monitoring API_ as a half title. Codename of the project is *javasimon*. We use word Simon as a synonym for a "monitor" in javadoc or on our wiki - of course we use it only for monitors based on the API. We write Simon mostly with capital S, Java Simon with space and javasimon as a one word with all lowercase. Word javasimon is probably best to use in search engines.
