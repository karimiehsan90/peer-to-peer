#!/usr/bin/env bash

docker "build -t net-master:${latest:-1} ."
