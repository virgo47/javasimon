# Internal development notes

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

Then, version should be set to the next snapshot, for example:
```
call mvn versions:set 4.0.2-SNAPSHOT
call mvn versions:commit
```