#
# the-java-network
#

#
# base-debian Dockerfile
#

FROM debian:jessie

# common dependencies
RUN \
  apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    curl \
    wget \
    git \
    openssh-client \
    unzip && \
  rm -rf /var/lib/apt/lists/*

# grab gosu for easy step-down from root - https://github.com/tianon/gosu
RUN gpg --keyserver ha.pool.sks-keyservers.net --recv-keys B42F6819007F00F88E364FD4036A9C25BF357DD4
RUN curl -o /usr/local/bin/gosu -SL "https://github.com/tianon/gosu/releases/download/1.4/gosu-$(dpkg --print-architecture)" \
  && curl -o /usr/local/bin/gosu.asc -SL "https://github.com/tianon/gosu/releases/download/1.4/gosu-$(dpkg --print-architecture).asc" \
  && gpg --verify /usr/local/bin/gosu.asc \
  && rm /usr/local/bin/gosu.asc \
  && chmod +x /usr/local/bin/gosu

#
# java Dockerfile
#

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
  echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list && \
  echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list && \
  apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886 && \
  apt-get update && apt-get install -y \
    oracle-java8-installer \
    oracle-java8-set-default && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer

#
# tomcat Dockerfile
#

# add our user and group first to make sure their IDs get assigned consistently, regardless of whatever dependencies get added
RUN groupadd -r tomcat && useradd -r -g tomcat tomcat

# Environment variables
ENV TOMCAT_MAJOR_VERSION 8
ENV TOMCAT_VERSION 8.0.23
ENV CATALINA_HOME /opt/apache-tomcat
ENV PATH ${CATALINA_HOME}/bin:${PATH}

# Download and setup Tomcat
RUN \
  mkdir -p ${CATALINA_HOME} && \
  apt-get update && \
  wget -P /tmp/ https://www.apache.org/dist/tomcat/tomcat-${TOMCAT_MAJOR_VERSION}/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz.md5 && \
  wget -P /tmp/ http://www.apache.org/dist/tomcat/tomcat-${TOMCAT_MAJOR_VERSION}/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
  cd /tmp && md5sum -c /tmp/apache-tomcat-${TOMCAT_VERSION}.tar.gz.md5 && \
  tar xzvf /tmp/apache-tomcat-${TOMCAT_VERSION}.tar.gz -C /tmp && \
  rm -rf /tmp/apache-tomcat-${TOMCAT_VERSION}/webapps/* && \
  mv /tmp/apache-tomcat-${TOMCAT_VERSION}/* ${CATALINA_HOME} && \
  rm -rf /tmp/* && \
  rm -rf /var/lib/apt/lists/*

RUN \
  cd ${CATALINA_HOME} && \
  chmod -R 755 conf/ && \
  chown -R tomcat:tomcat webapps/ temp/ logs/ work/

# Volumes
VOLUME ["/opt/apache-tomcat/webapps", "/opt/apache-tomcat/temp", "/opt/apache-tomcat/logs", "/opt/apache-tomcat/conf"]

#
# Java Network Dockerfile
#

# Environment variables
ENV GRADLE_VERSION 2.4
ENV GRADLE_HOME /opt/gradle
ENV PATH ${GRADLE_HOME}/bin:${PATH}

# Download and setup Gradle
RUN \
  mkdir -p ${GRADLE_HOME} && \
  apt-get update && \
  wget -P /tmp/ https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip && \
  unzip /tmp/gradle-${GRADLE_VERSION}-bin.zip -d /tmp && \
  mv /tmp/gradle-${GRADLE_VERSION}/* ${GRADLE_HOME} && \
  rm -rf /tmp/* && \
  rm -rf /var/lib/apt/lists/*

# Volumes
VOLUME ["/docker/volumes/src"]

# Entrypoint, cmd
COPY docker-entrypoint.sh /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

EXPOSE 8080

CMD ["clean", "build"]
