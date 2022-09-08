package producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class AsyncProducer {

    public static void main(String[] args) throws Exception {
        // 创建指定分组名的生产者
        DefaultMQProducer producer = new DefaultMQProducer("qiu");
        //自己的服务器地址
        producer.setNamesrvAddr("192.168.126.130:9876");

        // 启动生产者
        producer.start();

        while (true)
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
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
//        producer.shutdown();
    }
}