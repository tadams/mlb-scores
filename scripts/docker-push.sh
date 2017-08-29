#!/bin/sh
set -e

docker push ${DOCKER_HUB}/tca/nginx:${BUILD_NUMBER}
docker push ${DOCKER_HUB}/tca/mlb-scores:${BUILD_NUMBER}
