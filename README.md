## 1.Docker部署RocketMQ4.9.4
### 1.1 创建docker-compose.yml
```
version: '3.5'

services:
  rmqnamesrv:
    image: apache/rocketmq:4.9.4
    container_name: rmqnamesrv
    ports:
      - 9876:9876
    volumes:
      - ./rmqs/logs:/home/rocketmq/logs
      - ./rmqs/store:/home/rocketmq/store
    environment:
      JAVA_OPT_EXT: "-Duser.home=/home/rocketmq -Xms512M -Xmx512M -Xmn128m"
    command: ["sh","mqnamesrv"]
    networks:
        rmq:
          aliases:
            - rmqnamesrv
  rmqbroker:
    image: apache/rocketmq:4.9.4
    container_name: rmqbroker
    ports:
      - 10909:10909
      - 10911:10911
    volumes:
      - ./rmq/logs:/home/rocketmq/logs
      - ./rmq/store:/home/rocketmq/store
      - ./rmq/brokerconf/broker.conf:/etc/rocketmq/broker.conf
    environment:
        JAVA_OPT_EXT: "-Duser.home=/home/rocketmq -Xms512M -Xmx512M -Xmn128m"
    command: ["sh","mqbroker","-c","/etc/rocketmq/broker.conf","-n","rmqnamesrv:9876","autoCreateTopicEnable=true"]
    depends_on:
      - rmqnamesrv
    networks:
      rmq:
        aliases:
          - rmqbroker

  rmqconsole:
    image: styletang/rocketmq-console-ng:1.0.0
    container_name: rmqconsole
    ports:
      - 8180:8080
    environment:
        JAVA_OPTS: "-Drocketmq.namesrv.addr=rmqnamesrv:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false"
    depends_on:
      - rmqnamesrv
    networks:
      rmq:
        aliases:
          - rmqconsole

networks:
  rmq:
    name: rmq
    driver: bridge
```
### 1.2 创建启动文件create.sh
```
#!/usr/bin/env bash

# 创建目录
mkdir -p ./rmqs/logs
mkdir -p ./rmqs/store
mkdir -p ./rmq/logs
mkdir -p ./rmq/store

# 设置目录权限
chmod -R 777 ./rmqs/logs
chmod -R 777 ./rmqs/store
chmod -R 777 ./rmq/logs
chmod -R 777 ./rmq/store

# 下载并启动容器，且为 后台 自动启动
docker-compose up -d

```
### 1.3 启动部署RocketMQ
```
chmod +x create.sh && sh create.sh
```

## 2. 项目热部署配置
### 2.1 添加依赖
```
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-devtools</artifactId>
      <optional>true</optional> 
</dependency>
```

### 2.2 application.yml配置
```
spring.devtools.remote.restart.enabled=true
spring.devtools.restart.additional-paths=src/main/java
```

### 2.3 IDEA配置
```
1) File -> Settings -> Compiler -> Build Project automatically 进行勾选
2) ctrl + shift + alt + /, 选择Registry, 勾上Compiler autoMake allow when app running
```

## 3.启动consumer
```
启动：com.fwd.rocketmq.mq.consumer.TransactionConsumer
```

## 4.启动RocketmqApplication
```
启动：com.fwd.rocketmq.RocketmqApplication
```

## 5.调用接口发送RocketMQ事务消息
```
服务接口：http://[IP]:[PORT]/rocket/sendMessage/{message}
```