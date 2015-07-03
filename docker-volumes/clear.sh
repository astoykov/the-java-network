#!/bin/bash
set -e

# Delete service's cache

sudo rm -rf ./gradle/wrapper/*
cp ./empty ./gradle/wrapper

sudo rm -rf ./gradle/caches/*
cp ./empty ./gradle/caches

sudo rm -rf ./tomcat/logs/*
cp ./empty ./tomcat/logs

# Delete service's data

sudo rm -rf ./tomcat/temp/*
cp ./empty ./tomcat/temp

sudo rm -rf ./mysql/data/*
cp ./empty ./mysql/data
