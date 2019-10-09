package com.dbb;

/**
 * @author tc
 * @date 2019-10-09
 */
public class Run {

    public static void main(String[] args) {
        final MqMessage mqMessageC = new MqMessage();
        mqMessageC.setNameAddr("rocketmq.middleware.test.gdapi.net:9876");
        mqMessageC.setTopic("demo-rocketmq");
        mqMessageC.setConsumerGroupName("GID-DEMO");
        mqMessageC.setTags("abc");

        final MqMessage mqMessageP = new MqMessage();
        mqMessageP.setNameAddr("rocketmq.middleware.test.gdapi.net:9876");
        mqMessageP.setTopic("demo-rocketmq");
        mqMessageP.setProducerGroupName("GID-DEMO");
        mqMessageP.setTags("abc");

        final RocketConsumer consumer = new RocketConsumer(mqMessageC);
        final RocketProducer producer = new RocketProducer(mqMessageP);

        try {
            consumer.init();
            producer.initAndStart();

            Thread ct = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        consumer.listenerCheckStart();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "consumer-thread");

            Thread pt = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        producer.send();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "producer-thread");

            pt.start();
            ct.start();

            ct.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.shutdown();
            consumer.showdown();
        }
    }

}
