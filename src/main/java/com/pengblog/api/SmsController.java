package com.pengblog.api;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.qcloudsms.httpclient.HTTPException;
import com.google.gson.Gson;
import com.pengblog.bean.SendSmsResult;
import com.pengblog.service.IsmsService;

@Controller
@RequestMapping("/sms")
public class SmsController {

	@Autowired
	@Qualifier("smsService")
	private IsmsService smsService;
	
	
	@RequestMapping(value="/send.do", produces="application/json;charset=utf-8")
	@ResponseBody
	public Object sendSms(String phoneNumber) throws JSONException, HTTPException, IOException {
		
		
		SendSmsResult sendSmsResult = smsService.send(phoneNumber);
		
		Gson gson = new Gson();
		
		String retMapString = gson.toJson(sendSmsResult);
		
		return retMapString;
	}
	
}
