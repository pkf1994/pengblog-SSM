package com.pengblog.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.pengblog.bean.Administrator;
import com.pengblog.bean.LoginResult;
import com.pengblog.constant.PengblogConstant;
import com.pengblog.dao.IadministratorDao;
import com.pengblog.jwt.JwtUtil;
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
	
	@Override
	public LoginResult login(String username, String password) {

		Administrator administrator	= this.adminstratorDao.selectAdministratorByUsername(username);
		
		LoginResult loginResult = new LoginResult();
		
		if(administrator == null) {
			
			loginResult.setSuccess(false);
			
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
			
			loginResult.setSuccess(false);
			
			loginResult.setMessage("wrong password");
			
			logger.info(LogUtil.infoBegin);
			logger.info("管理员" + administrator.getAdministrator_username() + "登录失败，密码错误");
			logger.info(LogUtil.infoEnd);
			
		}else{	
			
			loginResult.setSuccess(true);
			
			loginResult.setMessage("sign in success");
			
			String token = JwtUtil.createJWT(UUID.randomUUID().toString(), administrator.getAdministrator_username(), PengblogConstant.JWT_EXPIRE_TIME_LONG);
			
			loginResult.setToken(token);
			
			loginResult.setValidTimeMillis(PengblogConstant.JWT_EXPIRE_TIME_LONG);
			
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
			
			loginResult.setSuccess(false);
			
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
		if(correctDynamicPassword == null) {
			loginResult.setSuccess(false);
			
			loginResult.setMessage("overdue dynamic password");
			
			
			logger.info(LogUtil.infoBegin);
			logger.info("登录失败，动态密码已过期");
			logger.info(LogUtil.infoEnd);
			
			return loginResult;
		}
		
		if(!correctDynamicPassword.equals(dynamicPassword)) {
			
			loginResult.setSuccess(false);
			
			loginResult.setMessage("wrong dynamic password");
			
			
			logger.info(LogUtil.infoBegin);
			logger.info("登录失败，输入了错误的动态密码");
			logger.info(LogUtil.infoEnd);
			
			return loginResult;
		}
		
		loginResult.setSuccess(true);
		
		loginResult.setMessage("sign in success");
		
		String token = JwtUtil.createJWT(UUID.randomUUID().toString(), phoneNumber, PengblogConstant.JWT_EXPIRE_TIME_LONG);
		
		loginResult.setToken(token);
		
		loginResult.setValidTimeMillis(PengblogConstant.JWT_EXPIRE_TIME_LONG);
		
		logger.info(LogUtil.infoBegin);
		logger.info("登录失败，输入了错误的动态密码");
		logger.info(LogUtil.infoEnd);
		
		return loginResult;
		
	}



}
