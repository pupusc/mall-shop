package com.wanmi.ms.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * KafkaProperties
 * Created by aqlu on 2017/2/9.
 */
@ConfigurationProperties(prefix = "kafka")
@Data
@SuppressWarnings({"WeakerAccess", "MismatchedQueryAndUpdateOfCollection"})
public class KafkaProperties {

    private Container container;

    private Consumer consumer = new Consumer();

    private Producer producer = new Producer();

    @Data
    public static class Consumer {
        private Boolean enable;

        private Map<String, Object> settings = new HashMap<>();

        public Consumer() {
            settings.put("bootstrap.servers", "localhost:9092");
            settings.put("group.id", UUID.randomUUID());
            settings.put("enable.auto.commit", true);
            settings.put("auto.commit.interval.ms", 1000);
            settings.put("session.timeout.ms", 10000);
            settings.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            settings.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        }
    }

    @Data
    public static class Producer {
        private Boolean enable;

        private Map<String, Object> settings = new HashMap<>();

        public Producer() {
            settings.put("bootstrap.servers", "localhost:9092");
            settings.put("acks", "all");
            settings.put("retries", 0);
            settings.put("batch.size", 16384);
            settings.put("linger.ms", 1);
            settings.put("buffer.memory", 33554432);
            settings.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            settings.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        }
    }

    @Data
    public static class Container {
        /**
         * Specify the container concurrency number of consumers to create.
         */
        private Integer concurrency;

        /**
         * Set to true if this endpoint should create a batch listener.
         */
        private boolean batchListener;

        /**
         * Set the idle event interval;
         * when set, an event is emitted if a poll returns no records and this interval has elapsed since a record was returned.
         */
        private Long idleEventInterval;

        /**
         * 只有在设置Consumer的enable.auto.commit=false时，此值才会生效；支持以下几种模式：
         * <ul>
         * <li>RECORD - Commit after each record is processed by the listener..</li>
         * <li>BATCH - Commit whatever has already been processed before the next poll.</li>
         * <li>TIME - Commit pending updates after {@link ContainerProperties#setAckTime(long) ackTime} has elapsed.</li>
         * <li>COUNT - Commit pending updates after {@link ContainerProperties#setAckCount(int) ackCount} has been exceeded.</li>
         * <li>COUNT_TIME - similar to TIME and COUNT but the commit is performed if either condition is true.</li>
         * <li>MANUAL - the message listener is responsible to acknowledge() the {@link Acknowledgment}; after which, the same semantics as BATCH< are applied.</li>
         * <li>MANUAL_IMMEDIATE - commit the offset immediately when the {@link Acknowledgment#acknowledge()} method is called by the listener.</li>
         * </ul>
         */
        private AbstractMessageListenerContainer.AckMode ackMode = AbstractMessageListenerContainer.AckMode.BATCH;

        /**
         * ackMode设置为AckMode.COUNT或AckMode.COUNT_TIME时，必须设置此参数；
         * 接收消息数量达到此数值时，会发送ack给broker.
         */
        private int ackCount = 1;

        /**
         * ackMode设置为AckMode.TIME或AckMode.COUNT_TIME时，必须设置此参数；单位：ms；
         * 接收消息时间达到此数值时，会发送ack给broker.
         */
        private long ackTime = 500;

        /**
         * The max time to block in the consumer waiting for records.
         */
        private volatile long pollTimeout = 1000;
    }
}
