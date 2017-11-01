package com.rtcomps.data.web.def;

import com.google.common.base.Stopwatch;

public class PageResult {
	public static final int CHK_SERVER_ERR = 1;
	public static final int CHK_SERVER_TIMEOUT_ERR = 2;
	
	private String successMsgs;
	private int errType;
	private Stopwatch stopwatch;
	
	public PageResult(String successMsgs, int errType) {
		this.successMsgs = successMsgs;
		this.errType = errType;
	}

	public String getSuccessMsgs() {
		return successMsgs;
	}

	public int getErrType() {
		return errType;
	}

}
