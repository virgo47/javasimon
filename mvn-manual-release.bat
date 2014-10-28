@ECHO OFF
set /P VERSION="Enter new version: "
set /P GPGPP="Enter GPG pass-phrase: "

set JAVA_HOME=%JAVA7_HOME%

call mvn versions:set -DnewVersion=%VERSION%
call mvn versions:commit
call mvn clean deploy -P sonatype-oss-release -Dgpg.passphrase=%GPGPP%

call mvn javadoc:aggregate-jar
call mvn source:aggregate

REM DISTRIBUTION ZIP (rename as needed)
if exist dist.zip del dist.zip
zip -j dist.zip console-embed\target\javasimon*.jar console-webapp\target\javasimon*.war ^
  core\target\javasimon*.jar examples\target\javasimon*.jar javaee\target\javasimon*.jar ^
  jdbc41\target\javasimon*.jar target\javasimon*.jar
