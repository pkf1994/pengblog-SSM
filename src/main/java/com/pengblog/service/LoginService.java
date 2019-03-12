package com.pengblog.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.pengblog.bean.Administrator;
import com.pengblog.bean.LoginResult;
import com.pengblog.dao.IadministratorDao;
import com.pengblog.redis.RedisUtil;
import com.pengblog.sms.SmsSender;
import com.pengblog.utils.LogUtil;
import com.pengblog.utils.Md5Util;
import com.pengblog.utils.MyTokenUtil;

@Service("loginService")
public class LoginService implements IloginService{
	
	private static final Logger logger = LogManager.getLogger(LoginService.class);
	
	@Autowired
	private IadministratorDao adminstratorDao;
	
	@Autowired
	@Qualifier("smsSender")
	private SmsSender smsSender;
	
	public static int validTimeMillis = 24*3600*1000;

	@Override
	public LoginResult login(String username, String password) {

		Administrator administrator	= this.adminstratorDao.selectAdministratorByUsername(username);
		
		LoginResult loginResult = new LoginResult();
		
		if(administrator == null) {
			
			loginResult.setLoginStatus("fail");
			
			loginResult.setMessage("non-existent users");
			
			logger.info(LogUtil.infoBegin);
			logger.info("登录失败，用户不存在");
			logger.info(LogUtil.infoEnd);
			
			return loginResult;
		}
		
		String salt = Md5Util.getSalt(administrator.getAdministrator_saltDate());
		
		String dbPassword = administrator.getAdministrator_password();
		
		password = Md5Util.getMD5(password + salt);
		
		Boolean rightAdministrator = Md5Util.verify(password, dbPassword);
		
		if(!rightAdministrator) {
			
			loginResult.setLoginStatus("fail");
			
			loginResult.setMessage("wrong password");
			
			logger.info(LogUtil.infoBegin);
			logger.info("管理员" + administrator.getAdministrator_username() + "登录失败，密码错误");
			logger.info(LogUtil.infoEnd);
			
		}else{	
			
			loginResult.setLoginStatus("success");
			
			loginResult.setMessage("sign in success");
			
			String token = MyTokenUtil.createJWT(validTimeMillis, administrator);
			
			loginResult.setToken(token);
			
			loginResult.setValidTimeMillis(validTimeMillis);
			
			logger.info(LogUtil.infoBegin);
			logger.info("管理员" + administrator.getAdministrator_username() + "登录成功，登录时长6000000ms");
			logger.info(LogUtil.infoEnd);
		}
		
		return loginResult;
	}

	@Override
	public LoginResult loginDynamic(String phoneNumber, String dynamicPassword) {
		
		LoginResult loginResult = new LoginResult();
		
		//检查用户输入的号码是否被认证
		if(!Arrays.asList(smsSender.getAuthenticatedPhoneNumbers()).contains(phoneNumber)) {
			
			loginResult.setLoginStatus("fail");
			
			loginResult.setMessage("unauthorized phone number");
			
			
			logger.info(LogUtil.infoBegin);
			logger.info("登录失败，未被认证的号码");
			logger.info(LogUtil.infoEnd);
			
			return loginResult;
		}
		
		//检查用户输入的密码是否正确
		//从redis中获取正确的动态密码
		String correctDynamicPassword = RedisUtil.getStringKV(phoneNumber, SmsService.redisDbIndex);
		// TODO Auto-generated method stub
		if(correctDynamicPassword != dynamicPassword) {
			
			loginResult.setLoginStatus("fail");
			
			loginResult.setMessage("wrong dynamic password");
			
			
			logger.info(LogUtil.infoBegin);
			logger.info("登录失败，输入了错误的动态密码");
			logger.info(LogUtil.infoEnd);
			
			return loginResult;
		}
		
		loginResult.setLoginStatus("success");
		
		loginResult.setMessage("sign in success");
		
		String token = MyTokenUtil.createJWT(validTimeMillis, phoneNumber);
		
		loginResult.setToken(token);
		
		loginResult.setValidTimeMillis(validTimeMillis);
		
		logger.info(LogUtil.infoBegin);
		logger.info("登录失败，输入了错误的动态密码");
		logger.info(LogUtil.infoEnd);
		
		return loginResult;
		
	}



}
