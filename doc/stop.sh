#!/bin/bash

if [ -f "./app.pid" ]; then
    PID=$(cat ./app.pid)
    kill $PID

    rm -f ./app.pid
fi
