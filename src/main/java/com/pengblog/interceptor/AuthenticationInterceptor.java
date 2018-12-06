package com.pengblog.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.peng.annotation.RequireToken;
import com.peng.exception.AuthenticationException;
import com.pengblog.bean.Administrator;
import com.pengblog.service.IadministratorService;
import com.pengblog.utils.LogUtil;
import com.pengblog.utils.MyTokenUtil;

public class AuthenticationInterceptor implements HandlerInterceptor{
	
	private static final Logger logger = LogManager.getLogger(AuthenticationInterceptor.class);
	
	@Autowired
	IadministratorService administratorService;
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 从 http 请求头中取出 token
        String token = request.getHeader("Authorization");
		
		// 如果不是映射到方法直接通过
		 if (!(handler instanceof HandlerMethod)) {
	            return true;
	        }
		 
		 HandlerMethod handlerMethod = (HandlerMethod) handler;
		 Method method = handlerMethod.getMethod();
	        //检查是否有RequireToken注释，无则跳过认证
	        if (method.isAnnotationPresent(RequireToken.class)) {
	        	RequireToken requireToken = method.getAnnotation(RequireToken.class);
	            if (requireToken.required()) {
	            	
	            	//检查是否存在token
	            	 if (token == null) {
	                     throw new AuthenticationException("此操作需要管理员权限，请登录");
	                 }
	            	 
	            	 //检查token是否合法
	            	 int administrator_id;
	            	 
	            	 try {
	            		 
						administrator_id = JWT.decode(token).getClaim("administrator_id").asInt();
						
					} catch (Exception e) {
						logger.info(LogUtil.infoBegin);
						logger.info("token解析异常");
						logger.info(LogUtil.infoEnd);
						throw new AuthenticationException("鉴权错误: token解析异常");
						
					}
					Administrator administrator = administratorService.getAdministratorById(administrator_id);
	            	 
	            	 if(administrator == null) {
	            		 
	            		 logger.info(LogUtil.infoBegin);
	            		 logger.info("非法用户");
	            		 logger.info(LogUtil.infoEnd);
	            		 
	            		 throw new AuthenticationException("非法用户！");
	            		
	            	 }
	            	 
	            	 Boolean verify =MyTokenUtil.isVerify(token, administrator);
	            	 
	                 if (!verify) {
	                	 
	                	 logger.info(LogUtil.infoBegin);
	                	 logger.info("非法访问");
	                	 logger.info(LogUtil.infoEnd);
	                	 
	                     throw new AuthenticationException("非法访问！");
	                     
	                 }
	                 
	                 logger.info(LogUtil.infoBegin);
	                 logger.info("鉴权通过");
	                 logger.info(LogUtil.infoEnd);
	                 
	                 return true;
	            	 
	            }
	        }
	        
	        
	        return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
