package com.wanmi.sbc.customer.configuration.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * <p>JPA Repository分开注入</p>
 * Created by of628-wenzhi on 2019-11-14-19:00.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.wanmi.sbc.customer")
public class JpaConfiguration {
}
