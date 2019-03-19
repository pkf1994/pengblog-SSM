package com.pengblog.bean;

public class SubmitCommentResult {
	
	private Boolean success;
	
	private String message;
	
	private int commentIdJustSubmit;

	public int getCommentIdJustSubmit() {
		return commentIdJustSubmit;
	}

	public void setCommentIdJustSubmit(int commentIdJustSubmit) {
		this.commentIdJustSubmit = commentIdJustSubmit;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
