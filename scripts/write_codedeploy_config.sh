#!/bin/bash

set -e

mkdir -p /var/codedeploy/studentapp

cat <<EOF >/var/codedeploy/studentapp/env.properties
APPLICATION_NAME=$APPLICATION_NAME
DEPLOYMENT_GROUP_NAME=$DEPLOYMENT_GROUP_NAME
DEPLOYMENT_ID=$DEPLOYMENT_ID
EOF