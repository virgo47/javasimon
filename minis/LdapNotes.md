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


## LDAP/AD authentication options

"User" means user being authenticated, "technical user" is dedicated user in LDAP that helps with
this process. Username is the name authenticated user provided, password typically means his
password (unless password of the technical user is clear from the context). Various filter/patterns
use Java messsage format syntax (as used in Spring LDAP/Security as well).

In any case using LDAPS is strongly recommended (`ldaps://host:636/...`).


### Bind to technical user, search for user, bind to user

**This is the most typical way how to authenticate user in LDAP.** Technical user binds to LDAP
and then uses some search filter(s) to find the authenticated user object. Then DN of this object
is used for another bind with password provided by the user. This means that authenticated user
does not need any search access for the directory, only bind must be allowed.

This is also crucial when DN for bind cannot be constructed from username directly, e.g. on AD
you'd want to login with `userlogin` to domain `domain.local`, but your DN is likely something like
`CN=Name Surname, CN=Users, DC=domain, DC=local` and `userPrincipalName=userlogin@domain.local,
CN=Users, DC=domain, DC=local` is not a valid DN for login. Instead you need to search for the user
using the technical user with a filter like `(&(objectClass=user) (userPrincipalName={0}))`. This
filter requires using full `userlogin@domain.local` name, but the domain name can be part of the
search filter. (I'm not sure if it's possible to search for users in various domains in a single
AD, in which case username should contain domain too.)

Depending on LDAP capabilities re-bind from technical user can have different access (e.g.
you cannot bind to user directly, but you can rebind from technical user? not sure here). Also
it may be possible to setup firewall that binds from application server is allowed but not from
other hosts.


### Bind directly to user

In standard LDAP where we bind using DN we would need to try one or multiple DN patterns, e.g.
`uid={0},dc=example,dc=com`. In case of AD we can use either DN (with the problems mentioned
above, typically DN of AD user does not contain what you'd like to use as username) or you can
use `userlogin@domain.local` type directly (older NetBIOS domain name like `DOMAIN\username` does
not work). Again, if all users are from a single domain we can put domain name into "DN pattern"
(not DN in this case really) and use only username as the input.

If we don't need to read anything else from LDAP/AD, bind may be enough, but if we want to check
the username against some filter too each authenticated user must have search rights in the
directory. Searching may not be absolutely necessary, but allows to check for `class` and
`memberOf` attributes (among other).


### Bind to technical user, search for user, compare password

This is least recommended way. First two steps are the same like in the first option. After this
instead of bind to the user we execute [LDAP compare operation](https://www.ldap.com/the-ldap-compare-operation)
on the password attribute (typically `userPassword`). This, however, means we have to be able to
construct the content of the password attribute in the first place. If it looks something like
`{sha}base64string` it may be easy, but we have to know exactly the casing of the prefix (annoying)
and the algorithm. If the algorithm uses salt we're not able to encode the tested password without
getting the salt first - and that means reading the attribute *which may be forbidden for good
reasons*.

If we use bind we don't care about password format, algorithm, how it is stored, etc. We don't need
to see that attribute at all.