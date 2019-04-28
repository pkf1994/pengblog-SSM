package com.pengblog.sms;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.httpclient.HTTPException;
import com.pengblog.bean.SendSmsResult;

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
	
	
	public SendSmsResult send(String phoneNumber,String code,int expireMinutes) throws Exception {
		
		SendSmsResult sendSmsResult = new SendSmsResult();
		
		Set<String> allowPhoneNumbers = new HashSet<>(Arrays.asList(authenticatedPhoneNumbers));
		
		if(!allowPhoneNumbers.contains(phoneNumber)) {
			
			throw new Exception("Unauthorized Number");
		}
		
		 String[] params = {code,expireMinutes + ""};
		 SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
		 
		 ssender.sendWithParam("86", phoneNumber,
			        templateId, params, smsSign, "", "");
		 
		 sendSmsResult.setSuccess(true);
			
		 return sendSmsResult;
		 
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
