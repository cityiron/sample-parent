package com.dbb;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author tc
 * @date 2019-10-09
 */
public class RocketProducer {

    private MQProducer producer;

    private MqMessage mqMessage;

    public RocketProducer(MqMessage mqMessage) {
        this.mqMessage = mqMessage;
    }

    public void initAndStart() throws Exception {
        init();
        start();
    }

    public void init() throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
            DefaultMQProducer(mqMessage.getProducerGroupName());
        this.producer = producer;
        // Specify name server addresses.
        producer.setNamesrvAddr(mqMessage.getNameAddr());
    }

    public void send() throws Exception {
        for (int i = 0; i < 100; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(mqMessage.getTopic(),
                mqMessage.getTags(),
                ("Hello RocketMQ " +
                    i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            //Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }

    }

    public void shutdown() {
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }

    public void start() throws Exception {
        //Launch the instance.
        producer.start();
    }

}
