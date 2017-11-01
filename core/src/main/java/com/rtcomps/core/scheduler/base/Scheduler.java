package com.rtcomps.core.scheduler.base;

import static org.quartz.core.jmx.JobDataMapSupport.newJobDataMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rtcomps.core.scheduler.core.SchedulerJob;
import com.rtcomps.core.scheduler.def.ScheduleRequest;
import com.rtcomps.core.scheduler.def.ScheduleResult;
import com.rtcomps.core.scheduler.def.ScheduledJob;
import com.rtcomps.core.scheduler.dto.ScheduleDto;

@Component
public class Scheduler {
	private static final String JOB_KEY_DELIM="-";
	private final Logger log =  LoggerFactory.getLogger(Scheduler.class);

	@Autowired
	private SchedulerFactory schedulerFactory;
	
	public ScheduleResult scheduleJob(ScheduleRequest request) {
		ScheduleDto schedule = request.getSchedule();
		Trigger trigger = CronTriggerFactory.createTrigger(schedule.getStartTime(), schedule.getEndTime(),
				schedule.getRepeatInterval(), schedule.getWeekdays());
		Map<String,Object> data = new HashMap<String,Object>();
		data.put(ScheduleRequest.SCHEDULE_REQ, request);
		JobDataMap dataMap = newJobDataMap(data);
		JobKey jobKey = createJobKey(request);
		JobDetail jobDetail = JobBuilder.newJob().withIdentity(jobKey).ofType(SchedulerJob.class).setJobData(dataMap).build(); 
		Date result = null;
		try {
			result = schedulerFactory.getScheduler().scheduleJob(jobDetail,trigger);
		} catch (SchedulerException e) {
			log.error("Failed to create  new Schedule : "+ request.getSummary());
			throw new RuntimeException("Error scheduling job: " + e.getMessage());
		}
		log.info("New schedule created: "+ request);
		return new ScheduleResult(jobKey.toString(),"Success", result.toString());
	}
	
	private JobKey createJobKey(ScheduleRequest request) {
		ScheduleIdentity sid = new ScheduleIdentity(request);
		return new JobKey(sid.getJobKey(),sid.getJobGroup());
	}

	public List<ScheduledJob> getSchedules(String groupName) {
		try {
			org.quartz.Scheduler quartzScheduler = schedulerFactory.getScheduler();
			Set<JobKey> jobKeys = quartzScheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
			List<ScheduledJob> scheduledJobs = new ArrayList<ScheduledJob>();
			for (JobKey jKey : jobKeys) {
				JobDataMap map = quartzScheduler.getJobDetail(jKey).getJobDataMap();
				ScheduleRequest request = (ScheduleRequest)map.get(ScheduleRequest.SCHEDULE_REQ);
				scheduledJobs.add(new ScheduledJob(jKey.getName(),jKey.getGroup(),request.getTimestamp().toString(),request.getSummary()));
			}
			log.info("Total scheduled jobs found for jobGroup="+groupName + ": "+scheduledJobs.size());
		    scheduledJobs.sort(Comparator.comparing(ScheduledJob::getCreateTime).reversed());
		    return scheduledJobs;
		} catch (Exception e ) {
			log.error("Error fetching schedules by groupName="+groupName,e);
			throw new RuntimeException(e);
		}
		
	}
	
	public void deleteSchedule(String jobKeyString) {
		try {
			String[] keyParts = jobKeyString.split(JOB_KEY_DELIM);
			org.quartz.Scheduler quartzScheduler = schedulerFactory.getScheduler();
			JobKey jobKey = new JobKey(keyParts[1], keyParts[0]);
			if (!quartzScheduler.checkExists(jobKey)) {
			   log.error("Deleting scheduled job failed: job not found - jobKey="+jobKeyString);
			   return;
			} 
			quartzScheduler.deleteJob(jobKey);
			log.info("Scheduled job deleted: jobKey="+jobKeyString);
		} catch (Exception e ) {
			log.error("Deleting scheduled job failed: jobKey="+jobKeyString,e);
			throw new RuntimeException(e);
		}
	}
		
}
