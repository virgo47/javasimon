@ECHO OFF
set /P VERSION="Enter new version: "
set /P GPGPP="Enter GPG pass-phrase: "

call mvn versions:set -DnewVersion=%VERSION%
call mvn version:commit
set JAVA_HOME=%JAVA6_HOME%
call mvn clean deploy -P sonatype-oss-release -Dgpg.passphrase=%GPGPP%