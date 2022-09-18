## 1.添加依赖
```
<dependency></br>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-devtools</artifactId>
      <optional>true</optional> 
</dependency>
```

## 2..application.yml配置
```
spring.devtools.remote.restart.enabled=true
spring.devtools.restart.additional-paths=src/main/java
```

## 3.IDEA配置
```
1) File -> Settings -> Compiler -> Build Project automatically 进行勾选
2) ctrl + shift + alt + /, 选择Registry, 勾上Compiler autoMake allow when app running
```

##4.启动consumer
```
启动：com.fwd.rocketmq.mq.consumer.TransactionConsumer
```

##5.启动RocketmqApplication
```
启动：com.fwd.rocketmq.RocketmqApplication
```

6.调用接口发送RocketMQ事务消息
```
服务接口：http://[IP]:[PORT]/rocket/sendMessage/{message}
```