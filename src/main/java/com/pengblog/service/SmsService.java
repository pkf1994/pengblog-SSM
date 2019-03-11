package com.pengblog.service;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.qcloudsms.httpclient.HTTPException;
import com.pengblog.captcha.CaptchaCodeGenerator;
import com.pengblog.redis.RedisUtil;
import com.pengblog.sms.SmsSender;

@Service("smsService")
public class SmsService implements IsmsService {

	private static int expireMinutes = 2;
	
	@Autowired
	@Qualifier("smsSender")
	private SmsSender smsSender;
	
	@Override
	public Map<String,Object> send(String phoneNumber) throws JSONException, HTTPException, IOException {
		
		String code = CaptchaCodeGenerator.generate();
		
		RedisUtil.setStringKV(phoneNumber, code, 2*60, 2);
		
		Map<String,Object> retMap = smsSender.send(phoneNumber, code, expireMinutes);
		
		return retMap;
	}

}
