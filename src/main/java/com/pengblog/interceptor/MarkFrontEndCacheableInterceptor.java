package com.pengblog.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.peng.annotation.FrontEndCacheable;
import com.peng.annotation.RecordClientIP;
import com.peng.annotation.RequireToken;
import com.peng.exception.AuthenticationException;
import com.pengblog.bean.Administrator;
import com.pengblog.constant.PengblogConstant;
import com.pengblog.redis.RedisUtil;
import com.pengblog.utils.LogUtil;
import com.pengblog.utils.MyTokenUtil;

public class MarkFrontEndCacheableInterceptor implements HandlerInterceptor {

	public static String X_IS_CACHEABLE = "X-Is-Cacheable";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		 
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		Method method = handlerMethod.getMethod();
		
		//检查是否有FrontEndCacheable注解
		if (method.isAnnotationPresent(FrontEndCacheable.class)) {
			
			response.setHeader(X_IS_CACHEABLE, "true");
			
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
	
		
	}
}
