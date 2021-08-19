package com.wanmi.sbc.crm.configuration;

import com.wanmi.sbc.common.configure.DataSourceProxyConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.wanmi.sbc.crm.*.mapper"}, sqlSessionFactoryRef = "sqlSessionFactoryBean")
public class MyBatisDataSourceProxyConfig extends DataSourceProxyConfig {

    @Autowired
    private MybatisProperties mybatisProperties;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources(mybatisProperties.getMapperLocations()[0]);
        sqlSessionFactoryBean.setMapperLocations(mapperLocations);
        sqlSessionFactoryBean.setConfiguration(mybatisProperties.getConfiguration());
        return sqlSessionFactoryBean.getObject();
    }

}
