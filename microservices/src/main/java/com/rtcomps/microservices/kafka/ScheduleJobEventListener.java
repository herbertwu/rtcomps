package com.rtcomps.microservices.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ScheduleJobEventListener {
	@Autowired
	private JobService jobService;

	
	@KafkaListener(topics = "${jobschedule.topic}", group = "job", containerFactory = "jobKafkaListenerContainerFactory")
    public void listenGroupTask(String jobName) {
        System.out.println("Received Messasge in group 'job': " + jobName);
		try {
			jobService.process(jobName);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private static class JobService {
		
		public void process(String jobName) {
			System.out.println("Finishing job: "+jobName);
		}
	}
}
