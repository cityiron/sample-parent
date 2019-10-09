package com.dbb;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author tc
 * @date 2019-10-09
 */
public class RocketConsumer {

    private MQPushConsumer consumer;

    private MqMessage mqMessage;

    private static final AtomicBoolean start = new AtomicBoolean(false);

    public RocketConsumer(MqMessage mqMessage) {
        this.mqMessage = mqMessage;
    }

    public void init() throws Exception {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(mqMessage.getConsumerGroupName());

        this.consumer = consumer;

        // Specify name server addresses.
        consumer.setNamesrvAddr(mqMessage.getNameAddr());

        // Subscribe one more more topics to consume.
        consumer.subscribe(mqMessage.getTopic(), mqMessage.getTags());
    }

    public void listener() throws Exception {
        // Register callback to execute on arrival of messages fetched from brokers.
        this.consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        start();
    }

    public void listenerCheckStart() throws Exception {
        // Register callback to execute on arrival of messages fetched from brokers.
        this.consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        start();
    }

    public void start() throws Exception {
        if (start.compareAndSet(false, true)) {
            //Launch the consumer instance.
            consumer.start();
        }
    }

    public void showdown() {
        consumer.shutdown();
    }

}
