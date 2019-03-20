package com.pengblog.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.pengblog.bean.CaptchaResult;
import com.pengblog.bean.LoginResult;
import com.pengblog.service.IcaptchaService;
import com.pengblog.service.IloginService;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	@Qualifier("loginService")
	private IloginService loginService;
	
	@Autowired
	@Qualifier("captchaService")
	private IcaptchaService captchaService;
	
	@RequestMapping(value="/login.do", produces="application/json;charset=utf-8")
	@ResponseBody
	public Object login(@RequestBody Map<String,String> loginInfo) throws Exception {
		
		String username = loginInfo.get("username");
		
		String password = loginInfo.get("password");
		
		String captchaId = loginInfo.get("captchaId");
		
		String captchaCode = loginInfo.get("captchaCode");
		
		CaptchaResult captchaResult = captchaService.checkCaptchaCode(captchaId,captchaCode);
		
		LoginResult loginResult = new LoginResult();
		
		if(captchaResult.getPass() == false) {
			
			ReturnVo.err("绕过验证码的非法登录");
			
		}
		
		loginResult = loginService.login(username, password);
		
		if(!loginResult.getSuccess()) {
			ReturnVo.err(loginResult.getMessage());
		}

		return ReturnVo.ok(loginResult);
		
		
	}
	
	
	@RequestMapping(value="/login_dynamic.do", produces="application/json;charset=utf-8")
	@ResponseBody
	public Object loginDynamic(@RequestBody Map<String,String> loginInfo) throws Exception {
		
		String phoneNumber = loginInfo.get("phoneNumber");
		
		String dynamicPassword = loginInfo.get("password");
		
		LoginResult loginResult = loginService.loginDynamic(phoneNumber, dynamicPassword);
		
		if(!loginResult.getSuccess()) {
			ReturnVo.err(loginResult.getMessage());
		}

		return ReturnVo.ok(loginResult);
		
		
	}
}
