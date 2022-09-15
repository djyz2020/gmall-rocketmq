package producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import utils.ThreadPoolUtil;

import java.io.UnsupportedEncodingException;

public class TransactionProducer {

    public static void main(String[] args) {
        TransactionMQProducer producer = new TransactionMQProducer("TransactionTopicProducer");

        producer.setNamesrvAddr("192.168.126.130:9876");

        producer.setExecutorService(ThreadPoolUtil.producerThreadPool);

        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("execute local transaction unknown");
                return LocalTransactionState.UNKNOW;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                System.out.println("sleep 5 seconds.");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("check local transaction success");
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        try {
            producer.start();

            Message msg = new Message("TransactionTopic",
                    "TagA",
                    "OrderId008",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));

            producer.send(msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        }

    }

}
