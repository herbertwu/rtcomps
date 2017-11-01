package com.rtcomps.core.scheduler.def;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rtcomps.core.scheduler.dto.ScheduleDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleRequest implements Serializable {
	private static final long serialVersionUID = -2881429361253327072L;
	public static final String SCHEDULE_REQ="scheduleReq";
	private  String topic;
	private  String id;
	private  String value;
	private  Date timestamp = new Date();
	private  ScheduleDto schedule;
	
	public ScheduleRequest() {
	}
	
	public ScheduleRequest(String topic, String id, String value, ScheduleDto schedule) {
		this.topic = topic;
		this.id=id;
		this.value = value;
		this.schedule = schedule;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ScheduleDto getSchedule() {
		return schedule;
	}
	public void setSchedule(ScheduleDto schedule) {
		this.schedule = schedule;
	}
	
	public String getSummary() {
		return "Schedule - " + schedule;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ScheduleRequest(String topic, String id, String value, Date timestamp, ScheduleDto schedule) {
		super();
		this.topic = topic;
		this.id = id;
		this.value = value;
		this.timestamp = timestamp;
		this.schedule = schedule;
	}

	
}
