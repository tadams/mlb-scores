#!/bin/sh
set -e

docker build -t ${DOCKER_HUB}/tca/nginx:${BUILD_NUMBER} ${WORKSPACE}/docker-nginx 

docker build -t ${DOCKER_HUB}/tca/mlb-scores:${BUILD_NUMBER} ${WORKSPACE}/docker-mlb-scores

