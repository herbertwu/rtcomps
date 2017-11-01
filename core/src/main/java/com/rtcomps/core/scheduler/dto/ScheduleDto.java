package com.rtcomps.core.scheduler.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.rtcomps.core.scheduler.def.DayOfWeek;
import com.rtcomps.core.scheduler.def.RepeatInterval;

public class ScheduleDto implements Serializable {
	private static final long serialVersionUID = 7974278216172596638L;
	private Date startTime;
	private Date endTime;
	private RepeatInterval repeatInterval;
	private List<DayOfWeek> weekdays;
	
	public ScheduleDto() {
	}
	
	
	public ScheduleDto(Date startTime, Date endTime, RepeatInterval repeatInterval, List<DayOfWeek> weekdays) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.repeatInterval = repeatInterval;
		this.weekdays = weekdays;
	}


	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public List<DayOfWeek> getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(List<DayOfWeek> weekdays) {
		this.weekdays = weekdays;
	}
	public RepeatInterval getRepeatInterval() {
		return repeatInterval;
	}
	public void setRepeatInterval(RepeatInterval repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	
	@JsonCreator
	public static ScheduleDto Create(String jsonString) {
		ScheduleConverter converter = new ScheduleConverter();
		return converter.convert(jsonString);
	}


	@Override
	public String toString() {
		return "[startTime=" + startTime + ", endTime=" + endTime + ", repeatInterval=" + repeatInterval
				+ ", weekdays=" + weekdays + "]";
	}
	
	
}
