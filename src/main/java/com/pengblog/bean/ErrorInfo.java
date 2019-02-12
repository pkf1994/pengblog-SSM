package com.pengblog.bean;

public class ErrorInfo {

	private boolean success;
	
	private String msg;
	
	private Object error;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getError() {
		return error;
	}

	public void setError(Object error) {
		this.error = error;
	}

	public ErrorInfo(boolean success, String msg, Object error) {
		super();
		this.success = success;
		this.msg = msg;
		this.error = error;
	}

	public ErrorInfo() {
		super();
	}
	
	
}
