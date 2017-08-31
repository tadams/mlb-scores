#!/bin/sh
set -e

docker build --tag ${DOCKER_HUB}/tca/nginx:${BUILD_NUMBER} \
             --build-arg GIT_DESCRIBE=$(git describe)      \
                   ${WORKSPACE}/docker-nginx
 
docker build -t ${DOCKER_HUB}/tca/mlb-scores:${BUILD_NUMBER} \
             --build-arg GIT_DESCRIBE=$(git describe)        \
                  ${WORKSPACE}/docker-mlb-scores

