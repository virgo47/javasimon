# Experiments with Keberos in webapp

Based on [spring-security-kerberos](http://projects.spring.io/spring-security-kerberos/) project. 
GitHub - clone: https://github.com/spring-projects/spring-security-kerberos.git

## Changes to YML in `sec-server-win-auth` sample

Change `application.yml` in `resources` (domain names, service principal, etc.):
```
server:
    port: 8080
app:
    ad-domain: naive.test
    ad-server: ldap://NAIVE-DC1.naive.test/
    service-principal: HTTP/neo.naive.test@naive.test
    keytab-location: c:/vagrant/tomcat.keytab
    ldap-search-base: cn=Users,dc=naive,dc=test
    ldap-search-filter: "(| (userPrincipalName={0}) (sAMAccountName={0}))"
```

## Register service principal name (SPN)

On the domain controller:
```
PS C:\Users\vagrant> setspn -S HTTP/neo.naive.test@naive.test vagrant1
Checking domain DC=naive,DC=test

Registering ServicePrincipalNames for CN=VAGRANT1,CN=Computers,DC=naive,DC=test
        HTTP/neo.naive.test@naive.test
Updated object
```

Note that `setspn -A ...` mentioned in spring-security-kerberos reference documentation in
*C.2 Setup Windows Domain Controller* is probably wrong. Switch `-A` does not exist and we also
need account name (here `vagrant1`).

## Keytab?

https://kb.iu.edu/d/aumh
A keytab is a file containing pairs of Kerberos principals and encrypted keys (which are derived
from the Kerberos password). You can use a keytab file to authenticate to various remote systems
using Kerberos without entering a password. However, when you change your Kerberos password, you
will need to recreate all your keytabs.

TODO: Do I need keytab file if I run the service on Windows server?

## Testing

We can use Internet Explorer or Firefox. For Firefox we need to set trusted URIs for negotiation.

* Run `java -jar sec-server-win-auth-1.0.2.BUILD-SNAPSHOT.jar` or `java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5555 -jar sec-server-win-auth-1.0.2.BUILD-SNAPSHOT.jar`
if you want remote debug (even from VirtualBox host!)
* If using Firefox the first time:
** go to `about:config`
** Set `network.negotiate-auth.trusted-uris` to `naive.test`
** Use `vagrant1.naive.test:8080` URL (or put short `vagrant1` to trusted URIs - not tested) 

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

StackOveflow contains [this question](http://stackoverflow.com/q/34227071/658826), no answer.
 