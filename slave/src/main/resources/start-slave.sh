#!/usr/bin/env bash

export BASEDIR=$(dirname "$0")
export BASEDIR=$BASEDIR/..
export APP_NAME="slave-1.0.jar"

cd $BASEDIR

jar uf lib/$APP_NAME -C conf/ .
mkdir -p logs
cd logs

java -jar ../lib/$APP_NAME
