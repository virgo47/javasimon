# Setup of my server


## "Metal"

AWS AMI server. 


## NGINX with PHP

I probably used something [like this](https://www.howtoforge.com/tutorial/installing-nginx-with-php7-fpm-and-mysql-on-ubuntu-16.04-lts-lemp/)
and combined it with AMI specific instructions (Google it).
[This Gist](https://gist.github.com/nrollr/56e933e6040820aae84f82621be16670) also seems interesting.


## HTTPS setup

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
installs some new packages (ptyhon27, etc.) but the command fails afterwards anyway.

Hm... can I still use Let's Encrypt?