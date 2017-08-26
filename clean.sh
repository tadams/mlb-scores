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

# clean Jenkins jobs
rm -rf /var/lib/jenkins/workspace/mlb-scores_DockerBuild
rm -rf /var/lib/jenkins/workspace/mlb-scores_Deploy
rm -rf /var/lib/jenkins/workspace/mlb-scores_Build

# clean build dir
rm -rf ~/mlb-scores/build
