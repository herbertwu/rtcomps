package com.rtcomps.core.scheduler.base;

import org.springframework.util.StringUtils;

import com.rtcomps.core.scheduler.def.ScheduleRequest;

public class ScheduleIdentity {
	private final ScheduleRequest schRequest;

	public ScheduleIdentity(ScheduleRequest schRequest) {
		this.schRequest = schRequest;
	}
	
    public String getJobKey() {
    	//Switch to persistence ID if needed
    	return String.valueOf(System.currentTimeMillis());
    }
    
    public String getJobGroup() {
    	return StringUtils.isEmpty(schRequest.getId())? schRequest.getTopic(): schRequest.getId();
    }
}
