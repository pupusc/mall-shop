package com.wanmi.sbc.order.configuration.mongo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;


@Configuration
public class MongoConfig {
    @Bean
    @ConditionalOnProperty(prefix = "mongo.transaction",value = "enable",havingValue = "true")
    MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}
