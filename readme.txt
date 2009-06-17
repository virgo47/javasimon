#summary readme.txt included in the download

= Java Simon - Simple Monitors for Java =

This software is distributed under the terms of the FSF Lesser Gnu Public License:
  * check "lgpl.txt" in the root directory of the project
  * or check it online http://www.gnu.org/licenses/lgpl.html

Check this readme online for updates: http://code.google.com/p/javasimon/wiki/Readme

== Build ==

  * Compiled Simon jars depend on:
    * JDK 1.5 or higher;
    * StAX API (`stax-api-1.0.1.jar` and `sjsxp.jar`) - this is NOT needed with JDK 1.6 and higher;
    * StAX API (`stax-api-1.0.1.jar` and `sjsxp.jar`) - this is NOT needed with JDK 1.6 and higher;
    * Java Simon JMX jar is compiled with and needs JDK 1.6 or higher (depends on JMX 1.4 that is not in JDK 1.5);
    * Java Simon Spring jar depends on `aopalliance.jar`, `commons-logging-1.1.1.jar`, `spring-aop.jar` and `spring-core.jar` - it is expected that these dependencies will be at least partially satisfied in a typical Spring environment.
  * Use "ant" to build the Simon:
    * You need both JDK 1.5 and JDK 1.6 to build Java Simon properly, set paths to both JDKs in the `build.properties` (the same way like `JAVA_HOME` variable);
    * ant uses included `lib` directory;
    * newly built jars are placed into `build` directory;
    * new ZIP archive is placed into the main project directory.
  * For Maven build, you have to set the same properties like for ant. Included libs are not utilized, standard Maven 2 dependency mechanism is used.
  * Few examples are not built by default (in package `other`). You may need other other libraries (JAMon) to compile and run them.

== Usage ==

There are two types of Simons available: `Counter` and `Stopwatch`. Counter tracks single long value, its maximum and minimum. Stopwatch measures time and tracks number of measurements (splits), total time, split minimum and maximum, etc.

=== Simon Manager ===

You obtain Simons from the `SimonManager`:
{{{
Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.examples.HelloWorld-stopwatch");
}}}

Here we obtained stopwatch Simon. If the Simon is accessed first time it is created. If you access existing Simon, type of the Simon must match - you can't create counter with the same name (unless you destroy the Simon first).

=== Stopwatch ===

Using stopwatch is simple:
{{{
Split split = stopwatch.start(); // returns split object
// here goes the measured code
long time = split.stop(); // returns the split time in ns
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
  * Homepage: http://www.javasimon.org
  * Project page: http://code.google.com/p/javasimon/
  * Download: http://code.google.com/p/javasimon/downloads/list
  * Javadoc API: http://javasimon.googlecode.com/svn/javadoc/api-2.0/index.html
  * Source browser: http://code.google.com/p/javasimon/source/browse/
  * Issue tracker: http://code.google.com/p/javasimon/issues/list
  * Ohloh page: http://www.ohloh.net/p/javasimon

Project uses following libraries:
  * Eval: https://eval.dev.java.net/ (core, not needed during runtime IF no filter callbacks are used)
    * `eval-0.4.jar`
  * TestNG: http://testng.org/ (test)
    * `testng-5.8-jdk15.jar`
  * Sun's StAX implementation: https://sjsxp.dev.java.net/ (not needed with JDK 1.6, but required for rebuild)
    * `stax-api-1.0.1.jar`
    * `sjsxp.jar`
  * Spring (core/aop): http://www.springsource.org/ (Spring integration)
    * `spring-core.jar`
    * `spring-aop.jar`
  * AOP alliance: http://aopalliance.sourceforge.net/ (Spring integration)
    * `aopalliance.jar`
  * Commons logging: http://commons.apache.org/logging/ (Spring integration)
    * `commons-logging-1.1.1.jar`
  * H2 Database: http://www.h2database.com/ (examples)
    * `h2.jar`

== Java Simon name ==

*Java Simon* is the official name of the project with _Simple Monitoring API_ as a half title. Codename of the project is *javasimon*. We use word Simon as a synonym for a "monitor" in javadoc or on our wiki - of course we use it only for monitors based on the API. We write Simon mostly with capital S, Java Simon with space and javasimon as a one word with all lowercase. Word javasimon is probably best to use in search engines.