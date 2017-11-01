package com.rtcomps.core.scheduler.def;

import java.io.Serializable;

public class RepeatInterval implements Serializable {
	private static final long serialVersionUID = -4412040402392707636L;
	public Integer repeatInterval;
	public TimeInterval repeatIntervalUnit;
	public Integer numberOfTimes;
	
	public RepeatInterval() {
	}
	public RepeatInterval(Integer repeatInterval, TimeInterval repeatIntervalUnit, Integer numberOfTimes) {
		this.repeatInterval = repeatInterval;
		this.repeatIntervalUnit = repeatIntervalUnit;
		this.numberOfTimes = numberOfTimes;
	}
	public Integer getRepeatInterval() {
		return repeatInterval;
	}
	public void setRepeatInterval(Integer repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	public TimeInterval getRepeatIntervalUnit() {
		return repeatIntervalUnit;
	}
	public void setRepeatIntervalUnit(TimeInterval repeatIntervalUnit) {
		this.repeatIntervalUnit = repeatIntervalUnit;
	}
	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}
	public void setNumberOfTimes(Integer numberOfTimes) {
		this.numberOfTimes = numberOfTimes;
	}
	@Override
	public String toString() {
		return repeatInterval + " " + repeatIntervalUnit
				+ ", numberOfTimes=" + numberOfTimes;
	}

	
}