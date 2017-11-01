package com.rtcomps.core.scheduler.def;

public class ScheduledJob {
	private final String id;
	private final String group;
	private final String createTime;
	private final String description;
	
	public ScheduledJob(String id, String group, String createTime, String description) {
		this.id = id;
		this.group = group;
		this.createTime = createTime;
		this.description = description;
	}


	public String getId() {
		return id;
	}
	
	public String getGroup() {
		return group;
	}
	
	public String getCreateTime() {
		return createTime;
	}

	public String getDescription() {
		return description;
	}
	
}
