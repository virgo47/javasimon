# Setup of my server


## "Metal"

AWS AMI server. 


## NGINX with PHP

I probably used something [like this](https://www.howtoforge.com/tutorial/installing-nginx-with-php7-fpm-and-mysql-on-ubuntu-16.04-lts-lemp/)
and combined it with AMI specific instructions (Google it).
[This Gist](https://gist.github.com/nrollr/56e933e6040820aae84f82621be16670) also seems interesting.


## HTTPS setup

Tutorials:
* Very good presentation of HTTPS basics: https://www.nginx.com/blog/nginx-https-101-ssl-basics-getting-started/
* HTTPS setup on NGINX: http://nginx.org/en/docs/http/configuring_https_servers.html


### First with self-signed cert

Let's try to setup HTTPS first before we try to get some trusted certificate.

```
sudo -i
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/ssl/private/nginx-selfsigne d.key -out /etc/ssl/certs/nginx-selfsigned.crt
```

Most answers are irrelevant for now, common name should be `virgo47.com` (or `www.virgo47.com`)
but with self-signed we will have browser warning anyway.

Next edit `/etc/nginx/nginx.conf` and enable TLS:

```
    server {
        listen       443 ssl;
        listen       [::]:443 ssl;
        server_name  virgo47.com;
        root         /home/ec2-user/virgo;

        ssl_certificate "/root/nginx-selfsigned.crt";
        ssl_certificate_key "/root/nginx-selfsigned.key";
        include /etc/nginx/default.d/*.conf;

        location / {
        }

        location /tmp {
                autoindex on;
        }

        error_page 404 /404.html;
            location = /40x.html {
        }

        error_page 500 502 503 504 /50x.html;
            location = /50x.html {
        }
    }
```

SSL works. Somehow. If browser can't get there (to https) at all we forgot to allow HTTPS (port 443)
in our Lightsail console, Firewall section.


### Proper certificate

I wanted free trusted certificate, I chose
[Let's Encrypt](https://letsencrypt.org/getting-started/).

There are couple of tutorials for AWS EC2 and Certbot, often about Lightsail and Let's Encrypt in
particular:
* http://www.alondiamant.com/2016-12-20-using-lets-encrypt-certificates-with-wordpress-on-amazon-lightsail
* https://coderwall.com/p/e7gzbq/https-with-certbot-for-nginx-on-amazon-linux
* https://nouveauframework.org/blog/installing-letsencrypts-free-ssl-amazon-linux/


#### What actually worked after problems with zope module

After reading comments in [this issue](https://github.com/certbot/certbot/issues/2872) I found

```
pip install pip --upgrade
pip install virtualenv --upgrade
virtualenv -p /usr/bin/python27 venv27
. venv27/bin/activate
git clone https://github.com/letsencrypt/letsencrypt
cd letsencrypt
# Nginx MUST be down for this
./letsencrypt-auto certonly --debug --standalone -d virgo47.com
```

This worked without problem. Certificate is [only for 90 days](https://letsencrypt.org/2015/11/09/why-90-days.html)
though, so automating the above (or the renew command) must be considered:

```
IMPORTANT NOTES:
 - Congratulations! Your certificate and chain have been saved at
   /etc/letsencrypt/live/virgo47.com/fullchain.pem. Your cert will
   expire on 2017-09-15. To obtain a new or tweaked version of this
   certificate in the future, simply run letsencrypt-auto again. To
   non-interactively renew *all* of your certificates, run
   "letsencrypt-auto renew"
 - Your account credentials have been saved in your Certbot
   configuration directory at /etc/letsencrypt. You should make a
   secure backup of this folder now. This configuration directory will
   also contain certificates and private keys obtained by Certbot so
   making regular backups of this folder is ideal.
 - If you like Certbot, please consider supporting our work by:

   Donating to ISRG / Let's Encrypt:   https://letsencrypt.org/donate
   Donating to EFF:                    https://eff.org/donate-le
```

Content of `/etc/letsencrypt/live/virgo47.com/`:
```
cert.pem  chain.pem  fullchain.pem  privkey.pem  README
```

`README` explains that `fullchain.pem` should probably used, not `cert.pem`.

This changes our lines in `nginx.conf` as so:

```
        ssl_certificate "/etc/letsencrypt/live/virgo47.com/fullchain.pem";
        ssl_certificate_key "/etc/letsencrypt/live/virgo47.com/privkey.pem";
```

Because Let's Encrypt does NOT offer wildcard certificates we need to repeat the process
for `www.virgo47.com` as well and set another server sections in `nginx.conf` for it
with different `server_name` and different paths to key and certificate. Not a big deal
(except for the duplication :-)).


#### Non-working standard process

I tried this one first, but I ended

```
sudo -i
mkdir tmp
cd tmp
wget https://dl.eff.org/certbot-auto
chmod a+x certbot-auto
./certbot-auto --nginx
```

Now the last command should help with NGINX support, but for Lightsail server it said:

```
FATAL: Amazon Linux support is very experimental at present...
if you would like to work on improving it, please ensure you have backups
and then run this script again with the --debug flag!
Alternatively, you can install OS dependencies yourself and run this script
again with --no-bootstrap.
```

So for certbot documentaion I chose *I'm using* **None of the above** *on* **Other UNIX**
which leads here: https://certbot.eff.org/#pip-other

But even with `./certbot-auto certonly` it still complaints. We can try to add `--debug` which
installs some new packages (ptyhon27, etc.) but the command fails afterwards anyway:

```
# ./certbot-auto certonly --standalne --debug -d virgo47.com -n
Error: couldn't get currently installed version for /root/.local/share/letsencrypt/bin/letsencrypt:
Traceback (most recent call last):
  File "/root/.local/share/letsencrypt/bin/letsencrypt", line 7, in <module>
    from certbot.main import main
  File "/root/.local/share/letsencrypt/local/lib/python2.7/dist-packages/certbot/main.py", line 7, in <module>
    import zope.component
  File "/root/.local/share/letsencrypt/local/lib/python2.7/dist-packages/zope/component/__init__.py", line 16, in <module>
    from zope.interface import Interface
ImportError: No module named interface
```

See the process above with virtualenv.


#### Renewal script


### Checking configuration

We can see in the browser that it works and what certification authority issued the certificate.
But we can also use `curl` to test the connection with various SSL protocols as we don't want
to support all the versions anyway.

```
curl --head -vi https://virgo47.com
```

This shows that by default TLSv1.2 is selected and also displays something about the certificate:
```
* SSL connection using TLSv1.2 / ECDHE-RSA-AES256-GCM-SHA384
* ALPN, server did not agree to a protocol
* Server certificate:
*        subject: CN=virgo47.com
*        start date: Jun 17 06:56:00 2017 GMT
*        expire date: Sep 15 06:56:00 2017 GMT
*        subjectAltName: virgo47.com matched
*        issuer: C=US; O=Let's Encrypt; CN=Let's Encrypt Authority X3
*        SSL certificate verify ok.
```

We can enforce other version of TLS/SSL like so:

```
curl --sslv2 --head -vi https://virgo47.com
...
* SSLv2 (OUT), , Client hello (1):
* Unknown SSL protocol error in connection to virgo47.com:443
* Closing connection 0
curl: (35) Unknown SSL protocol error in connection to virgo47.com:443
```

Using `--ssl` means to enable SSL/TLS in general, we can choose concrete version with `--sslv2`
or `--sslv3`. By default both are refused. We can also see that TLS is supported including
version 1.0:

```
curl --tlsv1.0 --head -vi https://virgo47.com
...
* SSL connection using TLSv1.0 / ECDHE-RSA-AES256-SHA
...
```

Using `--tlsv1` will negotiate any TLSv1.x, preferring 1.2, of course.

Supporting anything from TLS 1.0 higher is OK, 1.2 only would be a bit harsh for our purpose.

We can also test various cipher suits with `curl --ciphers ...`.


### What next?

HTTP/2  