package com.rtcomps.data.def;

public class ActionRunResult {
	private final ActionRunStatus status;
	private final int code;
	private final String message;
	private final String result;
	private final boolean hasOutput;
	
	public ActionRunResult(ActionRunStatus status, int code, String message, String result,boolean hasOutput) {
		this.status=status;
		this.code = code;
		this.message = message;
		this.result = result;
		this.hasOutput = hasOutput;
	}

	public ActionRunStatus getStatus() {
		return status;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getResult() {
		return result;
	}

	public boolean hasOutput() {
		return hasOutput;
	}
	
	public boolean isHasOutput() {
		return hasOutput;
	}

	@Override
	public String toString() {
		return "ActionRunResult [status=" + status + ", code=" + code + ", message=" + message + ", result=" + result
				+ ", hasOutput=" + hasOutput + "]";
	}

	public static ActionRunResult completed() {
		return new ActionRunResult(ActionRunStatus.COMPLETED, 0, null, null, false);
	}
	
	public static ActionRunResult failed(String errMsgs) {
		return new ActionRunResult(ActionRunStatus.FAILED, 1, errMsgs, null, false);
	}
	
}
