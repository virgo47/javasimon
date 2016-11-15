# Experiments with Keberos in webapp

Based on [spring-security-kerberos](http://projects.spring.io/spring-security-kerberos/) project. 
GitHub - clone: https://github.com/spring-projects/spring-security-kerberos.git

What is the scope of separate related project [Spring-Security-Kerberos-with-ActiveDirectory](https://github.com/grantcermak/Spring-Security-Kerberos-with-ActiveDirectory)?
While the issue says [SES-94: Add Active Directory support to spring-security-kerberos](https://github.com/spring-projects/spring-security-kerberos/issues/63)
it seems it only:

> I have a set of changes prepared that add Active Directory support for decoding the PAC
> to get the groups to which the Active Directory user belongs. This issue will track the
> submission of this change set.


## Unclear part of documentation

Open questions about Linux mentioned in the docs: https://github.com/spring-projects/spring-security-kerberos/issues/94

I tried to follow http://docs.spring.io/autorepo/docs/spring-security-kerberos/1.0.2.BUILD-SNAPSHOT/reference/htmlsingle/#samples-sec-server-win-auth
but got confused about two places mentioning Linux:

> Important
> You may need to use custom kerberos config with Linux either by using
> -Djava.security.krb5.conf=/path/to/krb5.ini or GlobalSunJaasKerberosConfig bean.

And:

> I exported keytab file which is copied to linux server running tomcat.

What if I run the application on the Windows server (or any computer in that domain)? Are these
necessary or is it unknown/open question?

Also later in http://docs.spring.io/autorepo/docs/spring-security-kerberos/1.0.2.BUILD-SNAPSHOT/reference/htmlsingle/#setupwinkerberos
command `setspn -A ...` is used. This works, but `setspn` does not document -A in its help and
according to http://serverfault.com/questions/488876/setspn-s-vs-setspn-a use of `-S` is encouraged
now. If `-S` is OK for this, it may help developer with troubleshooting when checking `setspn`
reference/help.


## Changes to YML in `sec-server-win-auth` sample

Change `application.yml` in `resources` (domain names, service principal, etc.):
```
server:
    port: 8080
app:
    ad-domain: naive.test
    ad-server: ldap://naive-dc1.naive.test/
    service-principal: HTTP/mysvc.naive.test@naive.test
    keytab-location: c:/vagrant/tomcat.keytab
    ldap-search-base: cn=Users,dc=naive,dc=test
    ldap-search-filter: "(| (userPrincipalName={0}) (sAMAccountName={0}))"
```

Important to know that {0} parameter in the filter is what comes from the login form username
concatenated with `ad-domain` value like this `vagrant@naive.test`, but when `ad-domain` is `null`
only name is used. In my DC I had no user attribute containing `vagrant@naive.test`, so I tried to
put `ad-domain` null and also changed the filter to `(cn={0})`.

But username without domain is used only for bind - in my case still successful even without
domain name. Before the actual search method `ActiveDirectoryLdapAuthenticationProvider#searchRootFromPrincipal`
throws `BadCredentialsException` - which sucks. This means we have to add `vagrant@naive.test`
into our DC (LDAP) into some attribute anyway. Best way is the Windows way - open user
*Properties*, tab *Account* and set *User Logon Name* to `vagrant` with domain select set to
`@naive.test` (will be offered). This goes into `userPrincipalName` and we can use original filter
without changes.

## Preparing service on active directory

Follow [C.2 Setup Windows Domain Controller](http://docs.spring.io/autorepo/docs/spring-security-kerberos/1.0.2.BUILD-SNAPSHOT/reference/htmlsingle/#setupwinkerberos).

We will create account `mysvc` for our service (application). We will use normal user account, but
see lower for using Managed Service Account - this however is more complicated.

For convenience we first disable password expiration, so we don't have to regenerate keytab file
often (not tested/confirmed):
```
set-ADDefaultDomainPasswordPolicy -MaxPasswordAge 0
```

### a) Setting service with user account

TODO: using normal user

```
New-ADUser -SamAccountName mysvc -Name mysvc -AccountPassword (ConvertTo-SecureString -AsPlainText "mysvcpass" -Force) -Enabled $true -PasswordNeverExpires $true
```

I also tried delegation for the user after seeing this in one video, but I'm not sure if this
applies:

* In *Active Directory Users and Computers*, click on the `mysvc` user to open *Properties*.
* *Delegation* tab, choose *Trust this user for delegation to any service (Kerberos only)

### b) Setting managed service account (WIP)

TODO: Currently not finished, not reliable, requires more research.

This is alternative to use common user account, which is probably more correct, but also much
less convenient. We want to create managed service account for the service, e.g. `mysvc`. Following
*Active Directory, 5th Edition*, page 276 (*Managed Service Accounts* and on):
```
#Add-KdsRootKey -EffectiveImmediately # for production, but not really immediate
Add-KdsRootKey –EffectiveTime ((get-date).addhours(-10))
$svcpass = "mysvcpass" | ConvertTo-SecureString -asPlainText -Force
# Using -AccountPassword $svcpass fails with New-ADServiceAccount : Parameter set cannot be resolved using the specified named parameters.
# That seems to be password policy problem(?)
New-ADServiceAccount -SamAccountName mysvc -Name mysvc -DNSHostName naive-dc1.naive.test -PrincipalsAllowedToRetrieveManagedPassword @('VAGRANT1$','vagrant')
```
See [here](https://social.technet.microsoft.com/Forums/windowsserver/en-US/82617035-254f-4078-baa2-7b46abb9bb71/newadserviceaccount-key-does-not-exist?forum=winserver8gen)
why it may be better to set effective time in the past.

TODO: If we don't set the password for `mysvc` with `New-ADServiceAccount` we can modify it -
but this fails on me with `Set-ADAccountPassword : Access is denied`:
```
Set-ADAccountPassword 'CN=mysvc,CN=Managed Service Accounts,DC=naive,DC=test' -Reset -NewPassword (ConvertTo-SecureString -AsPlainText 'mysvc123' -Force)
```

On the host where the service will run we may use this - but it doesn't seem to be must and
this command is not available on Windows 10 anyway:
```
Install-AdServiceAccount mysvc
```

MISSING PIECES:

* When I cannot set the password (which currently I don't know how due to problems mentioned
above) - how can I use `ktpass` without password?

### Register service principal name (SPN)

On the domain controller:
```
PS C:\Users\vagrant> setspn -S HTTP/mysvc.naive.test@naive.test mysvc
Checking domain DC=naive,DC=test

Registering ServicePrincipalNames for CN=mysvc,CN=Users,DC=naive,DC=test
        HTTP/mysvc.naive.test@naive.test
Updated object
```

Note that `setspn -A ...` mentioned in spring-security-kerberos reference documentation in
*C.2 Setup Windows Domain Controller* is obsolete.


## Keytab

https://kb.iu.edu/d/aumh
A keytab is a file containing pairs of Kerberos principals and encrypted keys (which are derived
from the Kerberos password). You can use a keytab file to authenticate to various remote systems
using Kerberos without entering a password. However, when you change your Kerberos password, you
will need to recreate all your keytabs.

```
ktpass /out c:\vagrant\tomcat.keytab /mapuser mysvc@NAIVE.TEST /princ HTTP/mysvc.naive.test@naive.test /pass mysvcpass /ptype KRB5_NT_PRINCIPAL /crypto All
```

Now copy the file to the proper location (e.g. other host) - in our case `c:\vagrant` on Windows
10 VirtualBox.

Q: Do I need keytab file if I run the service on Windows server? A: If we use Java without native
APIs then yes.


## Testing

We can use Internet Explorer or Firefox. For Firefox we need to set trusted URIs for negotiation:

* Go to `about:config`
* Set `network.negotiate-auth.trusted-uris` to `naive.test`

Without this we are always presented with login form.

### Running the test

* Run `java -jar sec-server-win-auth-1.0.2.BUILD-SNAPSHOT.jar`
* Add `-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5555` to `java` command
if you want remote debug (even from VirtualBox host!)
* Navigate to `vagrant1.naive.test:8080` URL (or put short `vagrant1` to trusted URIs - not tested)
and click the link (or navigate http://vagrant1.naive.test:8080/hello directly).

Currently I run into exception:
```
2016-06-22 17:44:24.601 DEBUG 864 --- [nio-8080-exec-1] w.a.SpnegoAuthenticationProcessingFilter : Received Negotiate Header for request http://vagrant1.naive.test:8080/favicon.ico: Negotiate YHcGBisGAQUFAqBtMGugMDAuBgorBgEEAYI3AgIKBgkqhkiC9xIBAgIGCSqGSIb3EgECAgYKKwYBBAGCNwICHqI3BDVOVExNU1NQAAEAAACXsgjiBQAFADAAAAAIAAgAKAAAAAoAWikAAAAPVkFHUkFOVDFOQUlWRQ==
2016-06-22 17:44:24.601 DEBUG 864 --- [nio-8080-exec-1] o.s.s.authentication.ProviderManager     : Authentication attempt using org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider
2016-06-22 17:44:24.601 DEBUG 864 --- [nio-8080-exec-1] .a.KerberosServiceAuthenticationProvider : Try to validate Kerberos Token
2016-06-22 17:44:24.624  WARN 864 --- [nio-8080-exec-1] w.a.SpnegoAuthenticationProcessingFilter : Negotiate Header was invalid: Negotiate YHcGBisGAQUFAqBtMGugMDAuBgorBgEEAYI3AgIKBgkqhkiC9xIBAgIGCSqGSIb3EgECAgYKKwYBBAGCNwICHqI3BDVOVExNU1NQAAEAAACXsgjiBQAFADAAAAAIAAgAKAAAAAoAWikAAAAPVkFHUkFOVDFOQUlWRQ==

org.springframework.security.authentication.BadCredentialsException: GSSContext name of the context initiator is null
        at org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator$KerberosValidateAction.run(SunJaasKerberosTicketValidator.java:173)
        at org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator$KerberosValidateAction.run(SunJaasKerberosTicketValidator.java:153)
```

Now this error happened without keytab and without `krb5.ini`. When we added keytab, the same
result. Then I ran it with this `krb5.ini`:
```
[libdefaults]
  default_realm = NAIVE

[realms]
  NAIVE = {
    kdc = naive-dc1.naive.test
    default_domain = NAIVE.TEST
  }

[domain_realm]
  .naive.test = NAIVE
  naive.test = NAIVE
```

Java command (without debug options):
```
java -Djava.security.krb5.conf=C:\vagrant\krb5.ini -jar sec-server-win-auth-1.0.2.BUILD-SNAPSHOT.jar
```

Still the same exception. Wireshark shows **no communication with DC** at all.

StackOveflow contains [this question](http://stackoverflow.com/q/34227071/658826), no answer.


## Other input

Using [Krb5LoginModule](https://docs.oracle.com/javase/7/docs/jre/api/security/jaas/spec/com/sun/security/auth/module/Krb5LoginModule.html)
with `login.conf` (JAAS configuration):
```
SuiteKrbSample {
 com.sun.security.auth.module.Krb5LoginModule REQUIRED
  doNotPrompt=true 
  useTicketCache=true 
  debug=true;
};
```


## Troubleshooting

### Is YML configuration OK and does form login work?

Perhaps first we should see whether the LDAP search against DC works. This is best checked with
Wireshark, watching for LDAP packets between the host with application and the DC. This helped
me to fix my user in DC (YML configuration was OK, domain must not be null there).

Any success with login/password doesn't seem to affect the troubles with SPNEGO/Kerberos.

### Other

http://stackoverflow.com/questions/25289231/using-gssmanager-to-validate-a-kerberos-ticket

Kerberos is case sensitive?! See `echo %USERDNSDOMAIN%` in `cmd`. In our case it's `NAIVE.TEST`.
I followed the instructions, added keytab and INI file to the mix - no result, still
`BadCredentialsException: GSSContext name of the context initiator is null`.

Wireshark filter used for monitoring (add more ignored protocols as needed):
```
ip.addr == 192.168.151.101 && !(dhcpv6 || dns || browser || nbns || nbss)
```