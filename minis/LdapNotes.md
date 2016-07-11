## Playing with LDAP

### Server

Use [ApacheDS](http://directory.apache.org/apacheds/), download ZIP version, unzip and run
`bin\apacheds.bat`.

### Client

For full-blown graphical client get [Apache Directory Studio](http://directory.apache.org/studio/).
(DS means *directory server*, studio just coincidentally/confusingly has the same acronym).
Run `ApacheDirectoryStudio.exe`. If there is [a problem with JNI](http://stackoverflow.com/q/16779751/658826)
use the same 32/64-bit Java as the studio. In my case I downloaded 32-bit, so I created `run.bat`:
```
set JAVA_HOME="c:\Progra~2\Java\jdk1.8.0_73"
set PATH=%JAVA_HOME%\bin;%PATH%
ApacheDirectoryStudio.exe
```
Run it and [connect to LDAP server](https://directory.apache.org/studio/users-guide/ldap_browser/gettingstarted_create_connection.html).

## Using cURL as LDAP client

When *ldap-utils* are not available we can use `curl` instead as shown in
[this post](http://whataniceblogtitle.blogspot.sk/2014/03/curl-as-ldap-client.html). Example:
```
curl -u "uid=admin,ou=system:secret" "ldap://localhost:10389/dc=example,dc=com?*?sub?(&(uid=virgo) (sn=Richter))"
```
URL is based on the default setup for *ApacheDS*, used *user:password* as well. Format of the URL
can be found in [RFC 4516](https://docs.ldap.com/specs/rfc4516.txt). In our example we wanted all
attributes (`*` which is also default) we searched in the subtree as well (scope `sub`, `base`
is default) and finally we specified the search filter ([RFC 4515](https://tools.ietf.org/search/rfc4515)).

Scope `base` is virtually useless if we want to find some object under the baseDN, `one` searches
in direct children only (may be useful), `sub` is typical scenario searching the whole subtree.