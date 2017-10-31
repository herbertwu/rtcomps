package com.rtcomps.core.mail;

public class Attachement {
	private String path;
	private String disposition;
	private String description;
	private String name;
	
	public Attachement(String path, String disposition, String description, String name) {
		this.path = path;
		this.disposition = disposition;
		this.description = description;
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public String getDisposition() {
		return disposition;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}
}
