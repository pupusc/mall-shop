package com.wanmi.sbc.marketing.configuration.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * <p>JPA Repository分开注入</p>
 * Created by of628-wenzhi on 2019-11-14-19:00.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.wanmi.sbc.marketing.**.mongorepository")
@EnableJpaRepositories(basePackages = {"com.wanmi.sbc.marketing.**.repository", "com.soybean.marketing.**.repository"})
@EntityScan(basePackages = {"com.wanmi.sbc.marketing","com.soybean.marketing"})
public class JpaConfiguration {
}
