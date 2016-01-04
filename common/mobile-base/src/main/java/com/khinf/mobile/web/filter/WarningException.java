package com.khinf.mobile.web.filter;

public class WarningException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WarningException(String title) {
		super(title);
	}

	public WarningException(Throwable e) {
		super(e);
	}

	public WarningException(String title, Throwable e) {
		super(title, e);
	}

}
