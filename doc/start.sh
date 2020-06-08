#!/bin/bash

BASE_DIR=`dirname $(readlink -f $0)`

## 服务端地址，使用web管理方式端口
export SERVER_PORT=10080

## 日志配置，logback日志引擎
export LOG_CONFIG_PATH=classpath:config/logback-spring.xml
export LOG_DIR=${BASE_DIR}/logs
export LOG_LEVEL=info

## 数据库位置，使用sqlite3
export SQLITE_DATABASE_PATH=${BASE_DIR}/natcross.db3

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

if [ ! -d $CERT_BASE_PATH ]; then
    mkdir -p $CERT_BASE_PATH
fi

## 服务端web端口，可以进行可视化操作，以及提供natcross-boot-client的端口获取，client的NATCROSS_HTTP_SERVER变量后面的端口要和该值保持一致
export SERVER_PORT=10080
## https的支持，让web操作使用ssl通信，其实就是https，并没有毛用，记得改client的NATCROSS_HTTP_SERVER变量为https
export SERVER_SSL_ENABLED=false
#export SERVER_SSL_PROTOCOL='TLSv1.2'
#export SERVER_SSL_KEY_STORE_TYPE='PKCS12'
#export SERVER_SSL_KEY_STORE_PATH=
#export SERVER_SSL_KEY_STORE_PASSWORD=

## 客户端服务端口，即内网穿透客户端交互的主要服务端口
export NATCROSS_CLIENT_SERVICE_PORT=10010

JAVA_OPTS="-XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20"
nohup java ${JAVA_OPTS} -jar ./natcross-boot.jar 2>&1 > /dev/null &

APPID=$!
echo $APPID > ./app.pid
