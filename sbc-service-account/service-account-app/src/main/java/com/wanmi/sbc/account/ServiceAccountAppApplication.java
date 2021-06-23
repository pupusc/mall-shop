package com.wanmi.sbc.account;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.wanmi.sbc.common.configure.CompositePropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;


import java.net.InetAddress;


@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class},scanBasePackages = {"com.wanmi.sbc"})
@EnableAsync
@EnableDiscoveryClient
@Slf4j
@EnableFeignClients(basePackages = {"com.wanmi.sbc"})
@PropertySource(value = {"api-application.properties"}, factory = CompositePropertySourceFactory.class)
@EnableJpaAuditing
public class ServiceAccountAppApplication {

	public static void main(String[] args)  throws Exception {
		Environment env = SpringApplication.run(ServiceAccountAppApplication.class, args).getEnvironment();
		String port = env.getProperty("server.port", "8394");
		String actPort = env.getProperty("management.server.port", "8391");

		log.info("Access URLs:\n----------------------------------------------------------\n\t"
						+ "Local: \t\thttp://127.0.0.1:{}\n\t"
						+ "External: \thttp://{}:{}\n\t"
						+ "health: \thttp://{}:{}/act/health\n----------------------------------------------------------",
				port,
				InetAddress.getLocalHost().getHostAddress(),
				port,
				InetAddress.getLocalHost().getHostAddress(),
				actPort
		);
	}
}
