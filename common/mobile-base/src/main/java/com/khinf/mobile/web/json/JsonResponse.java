package com.khinf.mobile.web.json;

public class JsonResponse {
	// Success=true And code>0为警告。messsage为通用内容
	private boolean success;
	private String message;
	private int code;
	private Object data;
	private String server;
	private String time;
	private Throwable exception;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

}
