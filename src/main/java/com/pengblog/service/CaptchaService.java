package com.pengblog.service;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.pengblog.bean.CaptchaResult;
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
	public CaptchaResult checkCaptchaCode(String captchaId, String uncheckCaptchaCode) {
		
		CaptchaResult captchaResult = new CaptchaResult();

		String correctCaptchaCode = RedisUtil.getStringKV(captchaId, dbIndex);
		
		if(correctCaptchaCode == null) {
			
			captchaResult.setPass(false);
			captchaResult.setMessage("overdue");
			
			return captchaResult;
		}
		
		if(!correctCaptchaCode.equalsIgnoreCase(uncheckCaptchaCode)){
			
			captchaResult.setPass(false);
			captchaResult.setMessage("wrong");
			
			return captchaResult;
		}
		
		if(correctCaptchaCode.equalsIgnoreCase(uncheckCaptchaCode)) {
			
			captchaResult.setPass(true);
			
			return captchaResult;
		}
		
		return null;
	}
	
}
