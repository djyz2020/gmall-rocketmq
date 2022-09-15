package producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import utils.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncProducer {

    public static void main(String[] args) throws Exception {

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            FutureTask task = new FutureTask(() -> {
                // 创建指定分组名的生产者
                DefaultMQProducer producer = new DefaultMQProducer("TopicTestGroup");
                //自己的服务器地址
                producer.setNamesrvAddr("192.168.126.130:9876");

                // 启动生产者
                producer.start();

                while (true) {
                    try {
                        // 构建消息
                        Message msg = new Message("TopicTest",
                                "TagA",
                                "OrderID188",
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

            Future<?> future = ThreadPoolUtil.producerThreadPool.submit(task);
            futures.add(future);
        }
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}