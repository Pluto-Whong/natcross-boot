#!/bin/bash

BASE_DIR=`dirname $(readlink -f $0)`

## 服务端地址，使用web管理方式端口
export SERVER_PORT=10080

## 日志配置，logback日志引擎
export LOG_CONFIG_PATH=classpath:config/logback-spring.xml
export LOG_DIR=${BASE_DIR}/logs
export LOG_LEVEL=info

## 数据库位置，使用sqlite3
export SQLITE_DATABASE_PATH=./natcross.db3

## 服务端的签名key，散列算法，约等于对称密钥，客户端使用签名方式获取接口状态
export SERVER_SIGN_KEY=serverSignKey

## 交互密钥和签名key
export NATCROSS_AES_KEY='0PMudFSqJ9WsQrTC60sva9sJAV4PF5iOBjKZW17NeF4='
export NATCROSS_TOKEN_KEY=tokenKey

## 证书存放的基础路径
export CERT_BASE_PATH=${BASE_DIR}/ssl/
## 默认证书文件名，采用相对路径的方式，只支持pkcs12格式证书
#export DEFAULT_CERT_NAME=
## 默认证书密码
#export DEFAULT_CERT_PASSWORD=

## 客户端服务端口，即内网穿透客户端交互的主要服务端口
export NATCROSS_CLIENT_SERVICE_PORT=10010

JAVA_OPTS="-XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20"
nohup java ${JAVA_OPTS} -jar ./natcross-boot.jar 2>&1 > /dev/null &

APPID=$!
echo $APPID > ./app.pid
