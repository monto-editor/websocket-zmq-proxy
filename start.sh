#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

gradle_shadow_jar="$DIR/build/libs/websocket-zmq-proxy-all.jar"

if [ ! -f "$gradle_shadow_jar" ]; then
    printf "No jar found. Please build the project first.\n" >&2
    exit 99
fi

java -jar "$gradle_shadow_jar" \
     -source tcp://localhost:5000 \
     -sink tcp://localhost:5001
