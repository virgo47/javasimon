@ECHO OFF
set /P VERSION="Enter new version: "
set /P GPGPP="Enter GPG pass-phrase: "

set JAVA_HOME=%JAVA7_HOME%

call mvn versions:set -DnewVersion=%VERSION%
call mvn versions:commit
call mvn clean deploy -P sonatype-oss-release -Dgpg.passphrase=%GPGPP%

goto :eof

REM DISTRIBUTION ZIP (rename as needed) - skipped, not needed anymore
call mvn javadoc:aggregate-jar
call mvn source:aggregate

if exist dist.zip del dist.zip
zip -j dist.zip console-embed\target\javasimon-console-embed-%VERSION%.jar ^
  console-webapp\target\javasimon-console-webapp.war ^
  core\target\javasimon-core-%VERSION%.jar ^
  examples\target\javasimon-examples-%VERSION%.jar ^
  javaee\target\javasimon-javaee-%VERSION%.jar ^
  jdbc41\target\javasimon-jdbc41-%VERSION%.jar ^
  spring\target\javasimon-spring-%VERSION%.jar ^
  target\javasimon*.jar
