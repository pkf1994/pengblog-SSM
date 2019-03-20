package com.pengblog.jwt;

public class JwtTokenCheckResult {
	
	private Boolean success;
	
	private int errCode;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}


	
}
