package com.fwd.rocketmq.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import com.fwd.rocketmq.utils.ThreadPoolUtil;

@Slf4j
public class AsyncProducer {

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 50; i++) {
            ThreadPoolUtil.producerThreadPool.submit(() -> {
                System.out.println("thread start: " + Thread.currentThread().getName());
                // 创建指定分组名的生产者
                DefaultMQProducer producer = new DefaultMQProducer("TopicTestGroup");
                //自己的服务器地址
                producer.setNamesrvAddr("192.168.126.130:9876");

                // 启动生产者
                try {
                    producer.start();
                } catch (MQClientException e) {
                    e.printStackTrace();
                }

                while (true) {
                    try {
                        // 构建消息
                        Message msg = new Message("TopicTest",
                                "TagA",
                                "OrderId008",
                                "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));

                        // 同步发送
                        SendResult sendResult = producer.send(msg);

                        // 打印发送结果
                        System.out.printf("%s%n", sendResult);

                        // Thread.sleep(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
        }

        log.info("all producer threads start success.");

        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}