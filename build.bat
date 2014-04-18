REM you must have "mvn" on PATH and JAVA6_HOME and JAVA7_HOME environment variables set
REM if your JDKs lie in Program Files (or other path with spaces) prefer short directory names (use dir /x to find out)

REM MOST OF THE STUFF - JAVA 6
set JAVA_HOME=%JAVA6_HOME%
call mvn clean package

REM JAVA 7 STUFF
cd jdbc41
set JAVA_HOME=%JAVA7_HOME%
call mvn clean package

REM SOURCES AND JAVADOCS (using JDK6 fo Javadoc)
cd ..
set JAVA_HOME=%JAVA6_HOME%
call mvn javadoc:aggregate-jar
call mvn source:aggregate

REM DISTRIBUTION ZIP (rename as needed)
if exist dist.zip del dist.zip
zip -j dist.zip console-embed\target\javasimon*.jar console-webapp\target\javasimon*.war ^
  core\target\javasimon*.jar examples\target\javasimon*.jar javaee\target\javasimon*.jar ^
  jdbc4\target\javasimon*.jar jdbc41\target\javasimon*.jar ^
  target\javasimon*.jar