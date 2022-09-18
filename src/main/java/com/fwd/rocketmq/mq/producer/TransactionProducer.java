package com.fwd.rocketmq.mq.producer;

import com.fwd.rocketmq.utils.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class TransactionProducer {
    private static TransactionMQProducer producer = new TransactionMQProducer("TransactionTopicProducer");

    static {
        producer.setNamesrvAddr("192.168.126.130:9876");
        if (producer.getTransactionCheckListener() == null) {
            producer.setTransactionListener(new TransactionListener() {
                @Override
                public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                    log.error("execute local transaction unknown.");
                    return LocalTransactionState.UNKNOW;
                }

                @Override
                public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                    log.info("check local transaction success.");
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
            });
        }
        if (producer.getExecutorService() == null) {
            producer.setExecutorService(ThreadPoolUtil.producerThreadPool);
        }
        try {
            producer.start();
            log.info("producer start success.");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public String sendMessage(String messageBody) {
        Message msg = null;
        try {
            msg = new Message("TransactionTopic",
                    "TagA",
                    "OrderId008",
                    messageBody.getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.sendMessageInTransaction(msg, null);
            log.info("send message [{}] success.", messageBody);
        } catch (UnsupportedEncodingException | MQClientException e) {
            log.error(e.getLocalizedMessage());
            return "FAILURE";
        }
        return "SUCCESS";
    }

}