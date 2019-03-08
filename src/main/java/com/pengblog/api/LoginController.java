package com.pengblog.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
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
	public Object login(@RequestBody Map<String,String> loginInfo) {
		
		String username = loginInfo.get("username");
		
		String password = loginInfo.get("password");
		
		String captchaId = loginInfo.get("captchaId");
		
		String captchaCode = loginInfo.get("captchaCode");
		
		Map<String, Object> checkResultMap = captchaService.checkCaptchaCode(captchaId,captchaCode);
		
		Map<String,Object> loginMap = new HashMap<>();
		
		if((Boolean)checkResultMap.get("pass") == false) {
			
			loginMap.put("loginStatus", 0);
			
			loginMap.put("loginMsg", "绕过验证码的非法登录");
			
			Gson gson = new Gson();
			
			String retJson = gson.toJson(loginMap);
			
			return retJson;
			
		}
		
		loginMap = loginService.login(username, password);
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(loginMap);
		
		return retJson;
		
		/*Map retMap = new HashMap<>();
		
		retMap.put("username", loginInfo.get("username"));
		
		retMap.put("password", loginInfo.get("password"));
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(retMap);
		
		return retJson;*/
		
	}
	
	
}
