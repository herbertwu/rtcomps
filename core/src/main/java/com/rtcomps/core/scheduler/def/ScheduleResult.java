package com.rtcomps.core.scheduler.def;

public class ScheduleResult {
	private  String  id;
	private  String status;
	private  String message;
	private  String createTime;
	
	public ScheduleResult(String id, String status, String createTime) {
		this.id = id;
		this.status = status;
		this.createTime = createTime;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
