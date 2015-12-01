FROM ubuntu:14.04
MAINTAINER virgo47

# We want WGET in any case
RUN apt-get -qqy install wget
# Alpine?
# RUN apk add --update wget

# For 32-bit Java we need to enable 32-bit binaries
RUN dpkg --add-architecture i386
RUN apt-get -qqy update
RUN apt-get -qqy install libc6:i386 libncurses5:i386 libstdc++6:i386

ENV HOME /root

# Install 32-bit JAVA
WORKDIR $HOME
RUN wget -q --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebac kup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u60-b27/jdk-8u60-linux-i586.tar.gz
RUN tar xzf jdk-8u60-linux-i586.tar.gz
ENV JAVA_HOME $HOME/jdk1.8.0_60
ENV PATH $JAVA_HOME/bin:$PATH
