# Internal development notes

## Formatting

* Tabs are used for indenting with single-tab continuation indent. This way it's up to you
how you set tab size in your environment.
* Tabs are never used elsewhere, only at the start of the line. No other alignments are used
elsewhere except for single continuation indent.
* Line comment behind statement is separated with a single space.
* Opening braces are on the same line except for cases where that line is wrapped
(and aligned with single indent), in which case the brace is on the separate line
(IntelliJ IDEA option "Next line if wrapped").
* Generally prefer lines under 100 characters with tabs counted as 4.

## Testing

Java Simon utilizes [TestNG](http://testng.org). Where necessary [mockito](http://mockito.org/)
framework is used. (Generally I prefer not to use it unless really necessary, in our tests
it lead often to white-box testing assumptions and it was difficult to redisign code even a little
bit.)

## Handy Git stuff

Removes last two commits:
```
git reset --soft HEAD~2
```

Handy after release experiments, broken `mvn release:prepare`, etc. It's better to use `mvn release:rollback` and
`mvn release:clean` before that as it does not revert the actual file changes.

## Release

By far the easiest way is to use `mvn-manual-release.bat` placed in project's root directory.
After answering prompts for released versions (preferably without SNAPSHOT, unless we want to deploy
snapshot version, of course) and GPG passphrase, Maven will do the rest. There is no commit/push involved.
After everything runs OK, local version is set to specified version. One can perform some final touches
(only to documentation, not to sources!) and commit/push.

When all previous steps succeed, our artifacts are staged. If we want to promote them
(really, really Release). To promote them, you may use [this section](http://central.sonatype.org/pages/apache-maven.html#releasing-the-deployment-to-the-central-repository)
of their guide. Alternatively you can login into https://oss.sonatype.org/ and follow
[these instructions](http://central.sonatype.org/pages/releasing-the-deployment.html). 

Then, version should be set to the next snapshot, for example:
```
call mvn versions:set -DnewVersion=4.1.1-SNAPSHOT
call mvn versions:commit
```

## Javadoc

Javadoc needs to be updated when API is changed (should corellate with changes of major/minor
versions, not with patch versions). Javadoc is to be copied after release (see
`mvn-manual-release.bat`) from ... to branch called `gh-pages`, directory `api/<major.minor>`.
It is recommended to keep this branch in separate directory for convenience. Example:
```
# in git-bash after mvn-manual-release.bat (BAT was run in cmd)
mv javasimon/target/apidocs/ javasimon-pages/api/4.1
git add api/4.1
git commit -m "added 4.1 javadoc"
git push
# ...enter name/password
```
After `git commit`, `git push`, it is possible to see the Javadoc here: http://virgo47.github.io/javasimon/api/4.1/
(Change version number as necessary. It is not possible to browse `api` directory.)