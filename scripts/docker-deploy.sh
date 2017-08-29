#!/bin/sh
set -e

docker-compose -p tca-scores -H ${Environment} up -d
