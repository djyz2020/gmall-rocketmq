package com.fwd.rocketmq.mq.consumer;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import com.fwd.rocketmq.utils.ThreadPoolUtil;

import java.util.List;

@Slf4j
public class AsyncConsumer {
    public static void main(String[] args) {

        for (int i = 0; i < 50; i++) {
            ThreadPoolUtil.consumerThreadPool.submit(() -> {
                System.out.println("thread start: " + Thread.currentThread().getName());
                // Instantiate with specified consumer group name.
                DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("TopicTestGroup");

                // Specify name server addresses.
                consumer.setNamesrvAddr("192.168.126.130:9876");

                // Subscribe one more more topics to consume.
                System.out.println("subscribe topic [TopicTest] ");
                try {
                    consumer.subscribe("TopicTest", "*");
                } catch (MQClientException e) {
                    e.printStackTrace();
                }
                // Register callback to execute on arrival of messages fetched from brokers.
                System.out.println("register message listener.");
                consumer.registerMessageListener(new MessageListenerConcurrently() {

                    @Override
                    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                                    ConsumeConcurrentlyContext context) {
                        System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                        /*try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                });

                //Launch the consumer instance.
                try {
                    System.out.println("consumer begin to start.");
                    consumer.start();
                } catch (MQClientException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("consumer start success.");
                }
            });
        }

        System.out.println("all consumer threads start success.");

        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}