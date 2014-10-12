@ECHO OFF
set /P VERSION="Enter new version: "
set /P GPGPP="Enter GPG pass-phrase: "

call mvn versions:set -DnewVersion=%VERSION%
call mvn version:commit
set JAVA_HOME=%JAVA6_HOME%
call mvn clean deploy -P sonatype-oss-release -Dgpg.passphrase=%GPGPP%

REM would be great if "update-parent" worked for older versions, but it does not
REM that's why for jdbc41 parent version must be set manualy BEFORE running this script
pushd jdbc41
REM call mvn versions:update-parent -DparentVersion=%VERSION%
REM call mvn version:commit
set JAVA_HOME=%JAVA7_HOME%
call mvn clean deploy -P sonatype-oss-release -Dgpg.passphrase=%GPGPP%

popd