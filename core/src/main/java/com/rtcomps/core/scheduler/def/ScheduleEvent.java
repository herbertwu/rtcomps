package com.rtcomps.core.scheduler.def;

import java.time.Instant;

public class ScheduleEvent {
	private final String topic;
	private final Object value;
	private final Instant createTime = Instant.now();
	public ScheduleEvent(String topic, Object value) {
		this.topic = topic;
		this.value = value;
	}
	public String getTopic() {
		return topic;
	}
	public Object getValue() {
		return value;
	}
	public Instant getCreateTime() {
		return createTime;
	}
	@Override
	public String toString() {
		return "ScheduleEvent [topic=" + topic + ", value=" + value + ", createTime=" + createTime + "]";
	}

}
