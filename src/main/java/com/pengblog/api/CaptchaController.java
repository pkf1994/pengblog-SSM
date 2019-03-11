package com.pengblog.api;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.gson.Gson;
import com.pengblog.captcha.CaptchaCodeGenerator;
import com.pengblog.service.IcaptchaService;
import com.pengblog.utils.MyFileUtil;

import sun.misc.BASE64Encoder;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {
	
	@Autowired
	@Qualifier("captchaService")
	private IcaptchaService captchaService;
		
	@RequestMapping(value="/get_captcha.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getCaptcha(String captchaId) throws IOException {
		
		BufferedImage captchaImage = captchaService.generateCaptchaImage(captchaId);
		
		String base64Img = MyFileUtil.transferBufferedImageToBase64(captchaImage);
		
		return base64Img;
	}
	
	@RequestMapping(value="/check_captcha.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object checkCaptcha(String captchaId, String uncheckCaptchaCode) throws IOException {
		
		Map<String, Object> checkResultMap = captchaService.checkCaptchaCode(captchaId,uncheckCaptchaCode);
		
		Gson gson = new Gson();		
		
		String retJson = gson.toJson(checkResultMap);
		
		return retJson;
	}
}
