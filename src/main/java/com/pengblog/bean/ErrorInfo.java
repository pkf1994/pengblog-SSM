package com.pengblog.bean;

public class ErrorInfo {

	private boolean success;
	
	private String msg;
	
	private Object erroe;

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

	public Object getErroe() {
		return erroe;
	}

	public void setErroe(Object erroe) {
		this.erroe = erroe;
	}

	public ErrorInfo(boolean success, String msg, Object erroe) {
		super();
		this.success = success;
		this.msg = msg;
		this.erroe = erroe;
	}

	public ErrorInfo() {
		super();
	}
	
	
}
