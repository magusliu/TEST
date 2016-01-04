package com.khinf.mobile.web.util;

import java.util.Locale;

import org.daochong.lang.Message;
import org.daochong.lang.StringUtils;

public class JsonAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code;

	public JsonAccessException(int code) {
		super(Message.getInstance().getMessage(code, ThreadLocalContext.get(Locale.class)));
		this.code = code;
	}

	public JsonAccessException(int code, Object... params) {
		super(Message.getInstance().getMessage(code, ThreadLocalContext.get(Locale.class), params));
		this.code = code;
	}

	public JsonAccessException(int code, String message, Object... params) {
		super(StringUtils.value(Message.getInstance().getMessage(code, ThreadLocalContext.get(Locale.class), params),
				message));
		this.code = code;
	}

	public JsonAccessException(int code, Throwable e, Object... params) {
		super(StringUtils.value(Message.getInstance().getMessage(code, ThreadLocalContext.get(Locale.class), params),
				e.getMessage()), e);
		this.code = code;
	}

	public JsonAccessException(int code, String message, Throwable cause, Object... params) {
		super(StringUtils.value(Message.getInstance().getMessage(code, ThreadLocalContext.get(Locale.class), params),
				message), cause);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
