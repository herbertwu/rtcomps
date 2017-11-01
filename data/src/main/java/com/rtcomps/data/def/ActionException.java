package com.rtcomps.data.def;

public class ActionException extends RuntimeException {
	private static final long serialVersionUID = -6395984620352542313L;

	public ActionException() {
		super();
	}

	public ActionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActionException(String message) {
		super(message);
	}

	public ActionException(Throwable cause) {
		super(cause);
	}

}
