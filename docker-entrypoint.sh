#!/bin/bash
set -e

# Set permissions to volumes which require "service user" permissions
chown -R tomcat:tomcat /opt/apache-tomcat/webapps
chown -R tomcat:tomcat /opt/apache-tomcat/temp
chown -R tomcat:tomcat /opt/apache-tomcat/logs

if [ -f '/docker/volumes/src/build.gradle' ]; then
  # init
  rm -rf /tmp/*
  mkdir /tmp/src && cp -R /docker/volumes/src/* /tmp/src

  # build
  cd /tmp/src
  /tmp/src/gradlew "$@"

  # deploy
  rm -rf /opt/apache-tomcat/webapps/*
  mv /tmp/src/build/libs/javanetwork-0.0.1-SNAPSHOT.war /opt/apache-tomcat/webapps/javanetwork.war
  chown tomcat:tomcat /opt/apache-tomcat/webapps/*.war

  # clean up
  rm -rf /tmp/*
fi

# Run the service as "service user"
exec gosu tomcat catalina.sh run
