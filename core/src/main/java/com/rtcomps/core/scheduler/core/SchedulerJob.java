package com.rtcomps.core.scheduler.core;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rtcomps.core.scheduler.def.ScheduleEvent;
import com.rtcomps.core.scheduler.def.ScheduleRequest;
import com.rtcomps.core.scheduler.kafka.ScheduleEventProducer;


public class SchedulerJob implements Job {
	private final Logger log =  LoggerFactory.getLogger(SchedulerJob.class);
	
	@Autowired
	private ScheduleEventProducer eventProducer;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleEvent evt = createScheduleEvent(context);
		eventProducer.sendMessage(evt.getTopic(),evt.getValue().toString());
		log.info("Scheduled job message sent: ScheduleEvent="+evt);
	}

	private ScheduleEvent createScheduleEvent(JobExecutionContext context) {
		JobDataMap map = context.getJobDetail().getJobDataMap();
		ScheduleRequest request = (ScheduleRequest)map.get(ScheduleRequest.SCHEDULE_REQ);
		return new ScheduleEvent(request.getTopic(), request.getValue());
	}
}
