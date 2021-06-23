package com.wanmi.sbc.configure;

import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author: Geek Wang
 * @createDate: 2019/8/6 11:47
 * @version: 1.0
 */
@Configuration
@EnableScheduling
public class QuartzConfiguration {

	private static final String QUARTZ_PROPERTIES_NAME = "/quartz.properties";

	@Autowired
	private DataSource dataSource;

	/**
	 * 继承org.springframework.scheduling.quartz.SpringBeanJobFactory
	 * 实现任务实例化方式
	 */
	public static class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements
			ApplicationContextAware {

		private transient AutowireCapableBeanFactory beanFactory;

		@Override
		public void setApplicationContext(final ApplicationContext context) {
			beanFactory = context.getAutowireCapableBeanFactory();
		}

		/**
		 * 将job实例交给spring ioc托管
		 * 我们在job实例实现类内可以直接使用spring注入的调用被spring ioc管理的实例
		 * @param bundle
		 * @return
		 * @throws Exception
		 */
		@Override
		protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
			final Object job = super.createJobInstance(bundle);
			beanFactory.autowireBean(job);
			return job;
		}
	}

	/**
	 * 配置任务工厂实例
	 * @param applicationContext spring上下文实例
	 * @return
	 */
	@Bean
	public JobFactory jobFactory(ApplicationContext applicationContext) {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	/**
	 * 配置任务调度器
	 * 使用项目数据源作为quartz数据源
	 * @param jobFactory 自定义配置任务工厂
	 * @return
	 * @throws Exception
	 */
	@Bean(destroyMethod = "destroy",autowire = Autowire.NO)
	public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory,DataSource dataSource) throws Exception
	{
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		//将spring管理job自定义工厂交由调度器维护
		schedulerFactoryBean.setJobFactory(jobFactory);
		//设置覆盖已存在的任务
		schedulerFactoryBean.setOverwriteExistingJobs(true);
		//项目启动完成后，等待2秒后开始执行调度器初始化
		schedulerFactoryBean.setStartupDelay(2);
		//设置调度器自动运行
		schedulerFactoryBean.setAutoStartup(true);
		//设置上下文spring bean name
		schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");

		schedulerFactoryBean.setQuartzProperties(quartzProperties());
		schedulerFactoryBean.setDataSource(dataSource);

		return schedulerFactoryBean;
	}

	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_PROPERTIES_NAME));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}
}