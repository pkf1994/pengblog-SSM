package com.pengblog.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.peng.annotation.RecordClientIP;
import com.peng.annotation.RequireToken;
import com.peng.exception.AuthenticationException;
import com.pengblog.bean.Administrator;
import com.pengblog.redis.RedisUtil;
import com.pengblog.utils.LogUtil;
import com.pengblog.utils.MyTokenUtil;

public class RecordClientIPInterceptor implements HandlerInterceptor {

	
	public static int dbIndex = 3;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
				// 如果不是映射到方法直接通过
				if (!(handler instanceof HandlerMethod)) {
		        return true;
			}
				 
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				
				Method method = handlerMethod.getMethod();
				
				//检查是否有RecordClientIP注释，无则跳过认证
				if (method.isAnnotationPresent(RecordClientIP.class)) {
					
					String ip = request.getHeader("X-Real-IP");
					
					if(ip != null) {
						
						String timesStr = RedisUtil.getStringKV(ip, dbIndex);
						
						if(timesStr == null) {
							RedisUtil.setStringKV(ip, 1+"", 10*60, dbIndex);
						}
						
						if(timesStr != null) {
							int times = Integer.parseInt(timesStr);
							
							Long effectTime = RedisUtil.getEffectiveTime(ip, dbIndex);
							
							RedisUtil.setStringKV(ip, times + 1 + "", effectTime, dbIndex);
						}
						
						
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
