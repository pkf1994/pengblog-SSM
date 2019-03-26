package com.pengblog.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.peng.annotation.RecordClientIP;
import com.pengblog.constant.PengblogConstant;
import com.pengblog.redis.RedisUtil;

public class RecordClientIPInterceptor implements HandlerInterceptor {

	
	public static int dbIndex = 3;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
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
	
		 
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		Method method = handlerMethod.getMethod();
		
		//检查是否有RecordClientIP注释，无则跳过认证
		if (method.isAnnotationPresent(RecordClientIP.class)) {
			
			String ip = request.getHeader("X-Real-IP");
			
			if(ip != null) {
				
				String timesStr = RedisUtil.getStringKV(ip, dbIndex);
				
				if(timesStr == null) {
					RedisUtil.setStringKV(ip, 1+"", PengblogConstant.REDIS_RECORD_IP_TIME_SECOND, dbIndex);
				}else{
					int times = Integer.parseInt(timesStr);
					
					Long effectTime = RedisUtil.getEffectiveTime(ip, dbIndex);
					
					if(effectTime == -1) {
						effectTime = PengblogConstant.REDIS_RECORD_IP_TIME_SECOND;
					}
					
					RedisUtil.setStringKV(ip, times + 1 + "", effectTime, dbIndex);
				}
			}
		}
	}
}
