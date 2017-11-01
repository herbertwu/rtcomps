package com.rtcomps.data.web.def;

import java.util.Date;

public class Screenshot {
	private String fileName;
	private String fileType;
	private int fileSize;
	private Date created;
	
	public Screenshot(String fileName, String fileType, int fileSize,Date created) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.created = created;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public Date getCreated() {
		return created;
	}

	public int getFileSize() {
		return fileSize;
	}

	@Override
	public String toString() {
		return "Screenshot [fileName=" + fileName + ", fileType=" + fileType
				+ ", fileSize=" + fileSize + ", created=" + created + "]";
	}
}
