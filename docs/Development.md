## BUBU

## Handy Git stuff

Removes last two commits:
```
git reset --soft HEAD~2
```

Handy after release experiments, broken `mvn release:prepare`, etc. It's better to use `mvn release:rollback` and
`mvn release:clean` before that as it does not revert the actual file changes.