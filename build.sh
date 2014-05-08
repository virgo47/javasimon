#!/bin/bash
# you must have "mvn" on PATH and JAVA6_HOME and JAVA7_HOME environment variables set
# if your JDKs lie in Program Files (or other path with spaces) prefer short directory names (use dir /x to find out)

# MOST OF THE STUFF - JAVA 6
export JAVA_HOME=$JAVA6_HOME
mvn clean package

# JAVA 7 STUFF
cd jdbc41
export JAVA_HOME=$JAVA7_HOME
mvn clean package

# SOURCES AND JAVADOCS (using JDK6 fo Javadoc)
cd ..
export JAVA_HOME=$JAVA6_HOME
mvn javadoc:aggregate-jar
mvn source:aggregate

# DISTRIBUTION ZIP (rename as needed)
if [ -f dist.zip ]; then
  rm dist.zip
fi

zip -j dist.zip console-embed/target/javasimon*.jar console-webapp/target/javasimon*.war \
  core/target/javasimon*.jar examples/target/javasimon*.jar javaee/target/javasimon*.jar \
  jdbc4/target/javasimon*.jar jdbc41/target/javasimon*.jar target/javasimon*.jar
