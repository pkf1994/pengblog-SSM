package com.pengblog.service;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.pengblog.captcha.CaptchaCodeGenerator;
import com.pengblog.redis.RedisUtil;

@Service("captchaService")
public class CaptchaService implements IcaptchaService {
	
	@Autowired
	@Qualifier("captchaProducer")
    private DefaultKaptcha captchaProducer;
	
	private static int effectiveTime = 2*60;
	
	private static int dbIndex = 1;

	@Transactional
	public BufferedImage generateCaptchaImage(String captchaId) {
		
		String captchaCode = CaptchaCodeGenerator.generate();

		RedisUtil.setStringKV(captchaId, captchaCode, effectiveTime, dbIndex);
		
		BufferedImage captchaImage = captchaProducer.createImage(captchaCode);
		
		return captchaImage;
	}

	@Override
	public Map<String, Object> checkCaptchaCode(String captchaId, String uncheckCaptchaCode) {
		
		Map<String,Object> retMap = new HashMap<>();

		String correctCaptchaCode = RedisUtil.getStringKV(captchaId, dbIndex);
		
		if(correctCaptchaCode == null) {
			retMap.put("pass", false);
			retMap.put("status", "overdue");
			return retMap;
		}
		
		if(!correctCaptchaCode.equalsIgnoreCase(uncheckCaptchaCode)){
			retMap.put("pass", false);
			retMap.put("status", "wrong");
			return retMap;
		}
		
		if(correctCaptchaCode.equalsIgnoreCase(uncheckCaptchaCode)) {
			retMap.put("pass", true);
			return retMap;
		}
		
		return null;
	}
	
}
