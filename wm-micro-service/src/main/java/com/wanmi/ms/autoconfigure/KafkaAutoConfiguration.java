package com.wanmi.ms.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaBootstrapConfiguration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * KafkaAutoConfiguration
 * Created by aqlu on 2017/2/9.
 */
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
@ConditionalOnClass(KafkaBootstrapConfiguration.class)
@EnableKafka
public class KafkaAutoConfiguration {

    private KafkaProperties kafkaProperties;

    public KafkaAutoConfiguration(KafkaProperties kafkaProperties){
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    @ConditionalOnProperty(prefix = "kafka.consumer", value = "enable", havingValue = "true")
    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(kafkaProperties.getConsumer().getSettings())
        );
        factory.setBatchListener(kafkaProperties.getContainer().isBatchListener());
        factory.setConcurrency(kafkaProperties.getContainer().getConcurrency());
        factory.getContainerProperties().setIdleEventInterval(kafkaProperties.getContainer().getIdleEventInterval());
        factory.getContainerProperties().setAckMode(kafkaProperties.getContainer().getAckMode());
        factory.getContainerProperties().setAckCount(kafkaProperties.getContainer().getAckCount());
        factory.getContainerProperties().setAckTime(kafkaProperties.getContainer().getAckTime());
        factory.getContainerProperties().setPollTimeout(kafkaProperties.getContainer().getPollTimeout());
        return factory;
    }

    @Bean
    @ConditionalOnProperty(prefix = "kafka.producer", value = "enable", havingValue = "true")
    @ConditionalOnMissingBean(KafkaTemplate.class)
    public KafkaTemplate<Object, Object> kafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(kafkaProperties.getProducer().getSettings()));
    }
}
