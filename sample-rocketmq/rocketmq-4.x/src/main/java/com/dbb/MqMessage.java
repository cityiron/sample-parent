package com.dbb;

/**
 * @author tc
 * @date 2019-10-09
 */
public class MqMessage {

    private String nameAddr;

    private String topic;

    private String tags;

    private String consumerGroupName;

    private String producerGroupName;

    public String getNameAddr() {
        return nameAddr;
    }

    public void setNameAddr(String nameAddr) {
        this.nameAddr = nameAddr;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getConsumerGroupName() {
        return consumerGroupName;
    }

    public void setConsumerGroupName(String consumerGroupName) {
        this.consumerGroupName = consumerGroupName;
    }

    public String getProducerGroupName() {
        return producerGroupName;
    }

    public void setProducerGroupName(String producerGroupName) {
        this.producerGroupName = producerGroupName;
    }

}
