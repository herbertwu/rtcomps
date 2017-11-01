package com.rtcomps.core.scheduler.base;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.rtcomps.core.scheduler.config.AutoWiredJobFactory;


@Configuration
public class SchedulerFactory   {

	private StdSchedulerFactory  schedulerFactory;

	private Scheduler  scheduler;
	
	private String instanceName;

	private String instanceId;

	private String threadCount;

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private Environment environment;

	@PostConstruct
    public void init()  throws IOException, SchedulerException {
		schedulerFactory = new StdSchedulerFactory();
		Properties quartzProperties = new Properties();
		quartzProperties.setProperty("org.quartz.scheduler.instanceName", "spring-boot-quartz-demo");
		quartzProperties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
		quartzProperties.setProperty("org.quartz.threadPool.threadCount", "5");
		String[] profiles = this.environment.getActiveProfiles();
		String propertiesFileName = "quartz.properties";
//		if(profiles.length>0){
//			for(String profile:profiles){
//				if("local".equalsIgnoreCase(profile)){
//					propertiesFileName = "quartz-local.properties";
//					break;
//				}
//			}
//		}
		schedulerFactory.initialize((new ClassPathResource(propertiesFileName)).getInputStream());
	    scheduler = schedulerFactory.getScheduler();
		scheduler.setJobFactory(springBeanJobFactory());
        scheduler.start();
        // schedulerFactory.setDataSource(dataSource);
	}
	 
	public Scheduler getScheduler() {
		return this.scheduler;
	}

	private SpringBeanJobFactory springBeanJobFactory() {
		AutoWiredJobFactory jobFactory = new AutoWiredJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

}
