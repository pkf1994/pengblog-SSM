package com.pengblog.constant;

public class PengblogConstant {
	
	public final static String JWT_SECRET = "Peng Kaifan";
	
	public final static int JWT_ERRCODE_EXPIRE = 1;
	
	public final static int JWT_ERRCODE_FAIL = 2;
	
	public final static long JWT_EXPIRE_TIME_MILLI_SECOND_LONG = 24*3600*1000;
	
	public final static int JWT_EXPIRE_TIME_SECOND_LONG = 24*3600;
	
	public final static long REDIS_RECORD_IP_TIME_SECOND = 10*60;
	
	public final static int CAPTCHA_CODE_SIZE = 4;
	
	public final static int REDIS_TOKEN_DBINDEX = 4;
	
	public final static int REDIS_CAPTCHA_DBINDEX = 1;
}
