package com.pengblog.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengblog.bean.Administrator;
import com.pengblog.dao.IadministratorDao;
import com.pengblog.utils.LogUtil;
import com.pengblog.utils.Md5Util;
import com.pengblog.utils.MyTokenUtil;

@Service("loginService")
public class LoginService implements IloginService{
	
	private static final Logger logger = LogManager.getLogger(LoginService.class);
	
	@Autowired
	private IadministratorDao adminstratorDao;

	@Override
	public Map<String,Object> login(String username, String password) {

		Administrator administrator	= this.adminstratorDao.selectAdministratorByUsername(username);
		
		Map<String,Object> retMap = new HashMap<>();
		
		if(administrator == null) {
			
			retMap.put("loginStatus", 0);
			
			retMap.put("loginMsg", "wrong username");
			
			logger.info(LogUtil.infoBegin);
			logger.info("登录失败，用户不存在");
			logger.info(LogUtil.infoEnd);
			
			return retMap;
		}
		
		String salt = Md5Util.getSalt(administrator.getAdministrator_saltDate());
		
		String dbPassword = administrator.getAdministrator_password();
		
		password = Md5Util.getMD5(password + salt);
		
		Boolean rightAdministrator = Md5Util.verify(password, dbPassword);
		
		if(!rightAdministrator) {
			
			retMap.put("loginStatus", 0);
			
			retMap.put("loginMsg", "wrong password");
			
			logger.info(LogUtil.infoBegin);
			logger.info("管理员" + administrator.getAdministrator_username() + "登录失败，密码错误");
			logger.info(LogUtil.infoEnd);
			
		}else{	
			
			retMap.put("loginStatus", 1);
			
			retMap.put("loginMsg", "logged in successfully");
			
			String token = MyTokenUtil.createJWT(6000000, administrator);
			
			retMap.put("token", token);
			
			retMap.put("validTimeMillis", 6000000);
			
			logger.info(LogUtil.infoBegin);
			logger.info("管理员" + administrator.getAdministrator_username() + "登录成功，登录时长6000000ms");
			logger.info(LogUtil.infoEnd);
		}
		
		return retMap;
	}

}
