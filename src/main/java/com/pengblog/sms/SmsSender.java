package com.pengblog.sms;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.httpclient.HTTPException;

public class SmsSender {
	// 短信应用SDK AppID
	private int appid;
	
	// 短信应用SDK AppKey
	private String appkey;
	
	// 短信模板ID	
	private int templateId;
	
	// 签名
	private String smsSign;
	
	// 授权的号码
	private String[] authenticatedPhoneNumbers;
	
	
	public Map<String,Object> send(String phoneNumber,String code,int expireMinutes) throws JSONException, HTTPException, IOException {
		
		Map<String,Object> retMap = new HashMap<>();
		
		Set<String> allowPhoneNumbers = new HashSet<>(Arrays.asList(authenticatedPhoneNumbers));
		
		if(!allowPhoneNumbers.contains(phoneNumber)) {
			
			retMap.put("status", "fail");
			retMap.put("message", "Unauthorized Number");
			
			return retMap;
		}
		
		 String[] params = {code,expireMinutes + ""};
		 SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
		 
		 ssender.sendWithParam("86", phoneNumber,
			        templateId, params, smsSign, "", "");
		 
		 retMap.put("status", "success");
		 
		return retMap;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public void setSmsSign(String smsSign) {
		this.smsSign = smsSign;
	}

	public String[] getAuthenticatedPhoneNumbers() {
		return authenticatedPhoneNumbers;
	}

	public void setAuthenticatedPhoneNumbers(String[] authenticatedPhoneNumbers) {
		this.authenticatedPhoneNumbers = authenticatedPhoneNumbers;
	}
	
	
	
}
