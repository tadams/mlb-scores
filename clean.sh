#!/bin/sh

# clear history
history -c
history -w

# clean downloads
rm ~/Downloads/*

# clean firefox
rm ~/.mozilla/firefox/*.default/cookies.sqlite
rm ~/.mozilla/firefox/*.default/*.sqlite ~/.mozilla/firefox/*default/sessionstore.js
rm -r ~/.cache/mozilla/firefox/*.default/*

# remove all docker containers
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

# remove docker images
docker rmi -f $(docker images -a -q)
docker pull openjdk:alpine
docker pull nginx:alpine

# clean build dir
rm -rf ~/mlb-scores/build

# clean Jenkins jobs
sudo su jenkins
rm -rf /var/lib/jenkins/workspace/mlb-scores_DockerBuild
rm -rf /var/lib/jenkins/workspace/mlb-scores_Deploy
rm -rf /var/lib/jenkins/workspace/mlb-scores_Build
rm /var/lib/jenkins/logs/tasks/*
rm -rf /var/lib/jenkins/jobs/mlb-scores_Build/builds
exit
