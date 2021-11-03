package com.fangdeng.server.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        sqlSessionTemplateRef = "sqlSessionTemplate",
        basePackages = {"com.fangdeng.server.mapper"})
@EnableTransactionManagement

public class MyBatisConfig {


    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sentinelSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*Mapper.xml"));
        org.apache.ibatis.session.Configuration configuration = bean.getObject().getConfiguration();
        if (configuration == null) {
            configuration = new org.apache.ibatis.session.Configuration();
        }
        configuration.setMapUnderscoreToCamelCase(true);
        return bean.getObject();
    }

    @Bean("dataSource")
    @ConfigurationProperties(prefix ="spring.datasource")
    public DataSource dataSource() {
//        DriverManagerDataSource ds= new DriverManagerDataSource();
//        ds.setDriverClassName("com.mysql.jdbc.Driver");
//        ds.setUrl("dbc:mysql://rm-bp182fitehfamv41c.mysql.rds.aliyuncs.com:3306/sbc-goods?characterEncoding=UTF-8&&zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&connectTimeout=0");
//        ds.setUsername("fddsh_mall");
//        ds.setPassword("malladmin@2018");
//        return ds;
       return DataSourceBuilder.create().build();
    }

    @Bean("dataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
