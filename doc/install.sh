#!/bin/bash

rm -rf ./source_code

git clone https://github.com/Pluto-Whong/natcross-boot.git ./source_code

mvn clean compile package -Dmaven.test.skip=true -f ./source_code/

. ./stop.sh

rm -rf natcross-boot.jar
cp ./source_code/target/natcross-boot.jar ./
