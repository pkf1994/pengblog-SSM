package com.pengblog.service;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.qcloudsms.httpclient.HTTPException;
import com.pengblog.bean.SendSmsResult;
import com.pengblog.captcha.CaptchaCodeGenerator;
import com.pengblog.redis.RedisUtil;
import com.pengblog.serviceInterface.IsmsService;
import com.pengblog.sms.SmsSender;

@Service("smsService")
public class SmsService implements IsmsService {

	public static int expireMinutes = 2;
	
	public static int sizeOfDynamicPassword = 6;
	
	public static int redisDbIndex = 2;
	
	@Autowired
	@Qualifier("smsSender")
	private SmsSender smsSender;
	
	@Override
	public SendSmsResult send(String phoneNumber) throws JSONException, HTTPException, IOException {
		
		SendSmsResult sendSmsResult = new SendSmsResult();
		
		Long effectiveTimeRemain = RedisUtil.getEffectiveTime(phoneNumber, redisDbIndex);
		
		//检查距离上一次发送动态密码到同一号码的时长是否足够60s
		if(effectiveTimeRemain > 60) {
			
			sendSmsResult.setSuccess(false);
			sendSmsResult.setMessage("in cooldown time");
			
			return sendSmsResult;
		}
		
		String code = CaptchaCodeGenerator.generate(sizeOfDynamicPassword);
		
		RedisUtil.setStringKV(phoneNumber, code, expireMinutes*60, redisDbIndex);
		
		sendSmsResult = smsSender.send(phoneNumber, code, expireMinutes);
		
		return sendSmsResult;
	}

}
