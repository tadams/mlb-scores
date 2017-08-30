#!/bin/sh
set -e

# clear history
echo /dev/null > ~/.bash_history

# clean downloads
rm -f ~/Downloads/*

# clean firefox
rm -f ~/.mozilla/firefox/*.default/cookies.sqlite
rm -f ~/.mozilla/firefox/*.default/*.sqlite ~/.mozilla/firefox/*default/sessionstore.js
rm -f -r ~/.cache/mozilla/firefox/*.default/*

# remove all docker containers
CONTAINER_IDS="$(docker ps -a -q)"
if [ -z "$CONTAINER_IDS" ]
then
  echo "No containers..."
else 
  docker stop $CONTAINER_IDS
  docker rm $CONTAINER_IDS
fi 


# remove docker images
IMAGE_IDS="$(docker images -a -q)"
if [ -z "$IMAGE_IDS" ]
then
  echo "No images..."
else
  docker rmi -f $IMAGE_IDS 
fi 

docker pull openjdk:alpine
docker pull nginx:alpine

# clean build dir
rm -rf ~/mlb-scores/build

# remove files created in workshop
rm -f docker-compose.yml
rm -f ~/mlb-scores/docker-nginx/Dockerfile
rm -f ~/mlb-scores/docker-mlb-scores/Dockerfile
cd ~/mlb-scores/scripts
rm -f docker*.sh

# clean Jenkins jobs
rm -rf /var/lib/jenkins/workspace/mlb-scores_DockerBuild
rm -rf /var/lib/jenkins/workspace/mlb-scores_Deploy
rm -rf /var/lib/jenkins/workspace/mlb-scores_Build
rm -f /var/lib/jenkins/logs/tasks/*
rm -rf /var/lib/jenkins/jobs/mlb-scores_Build/builds



