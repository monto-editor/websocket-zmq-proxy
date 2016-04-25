#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

eclipse_jar="$DIR/dist/websocket-zmq-proxy.jar"
gradle_jar="$DIR/build/libs/websocket-zmq-proxy.jar"

if [ -f "$eclipse_jar" ]; then
    jar="$eclipse_jar"
elif [ -f "$gradle_jar" ]; then
    jar="$gradle_jar"
else
    printf "No jar found. Please build the project first.\n" >&2
    exit 99
fi

java -jar "$jar" \
     -source tcp://*:5000 \
     -sink tcp://*:5001 \
     -discovery tcp://*:5005 \
     -configuration tcp://*:5007 \
     -debug
