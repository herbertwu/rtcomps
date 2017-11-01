package com.rtcomps.data.web.def;

import java.io.File;

import com.rtcomps.data.FileStorageService;


public class ScreenOptions {
	
	private String sessionId;
	private Exception exception;
	private String screenshotImgType="png"; // marker here
	private FileStorageService fileStorageService;
	
	public ScreenOptions(String sessionId, Exception exception,
			FileStorageService fileStorageService) {
		this.sessionId = sessionId;
		this.exception = exception;
		this.fileStorageService = fileStorageService;
	}


	public String getSessionId() {
		return sessionId;
	}

	public Exception getException() {
		return exception;
	}
	
	public String getScreenshotImgType() {
		return screenshotImgType;
	}

	public FileStorageService getFileStorageService() {
		return fileStorageService;
	}

	@Override
	public String toString() {
		return "ScreenOptions [sessionId=" + sessionId + ", exception="
				+ ((exception==null)? "" : exception.getMessage().substring(0,10))+"]";
	}
	
}
