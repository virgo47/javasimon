REM you must have "gradle" on PATH and JAVA7_HOME environment variables set
REM if your JDKs lie in Program Files (or other path with spaces) prefer short directory names (use dir /x to find out)

set JAVA_HOME=%JAVA7_HOME%
call gradle clean build