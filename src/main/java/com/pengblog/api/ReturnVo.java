package com.pengblog.api;

import com.google.gson.Gson;

public class ReturnVo {
	
	public static Object ok(Object retData) {
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(retData);
		
		return retJson;
	}
	
	public static Object err(String msg) throws Exception {
		
		throw new Exception(msg);
		
	}

}
