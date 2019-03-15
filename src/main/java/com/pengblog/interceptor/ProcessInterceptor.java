/**
 * 
 */
package com.pengblog.interceptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Administrator
 *
 */
public class ProcessInterceptor implements HandlerInterceptor {
	
	
	private static String[] allowDomains = {"http://localhost:3000","http://localhost:9000","http://localhost:3001","http://localhost:80","http://119.29.207.235:80","http://127.0.0.1:80","http://192.168.1.106:3001"};
	 
	  
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		  
		Set<String> allowOrigins = new HashSet<>(Arrays.asList(allowDomains));
		
		String origin = httpServletRequest.getHeader("Origin");
		
		
		
		if(allowOrigins.contains(origin)) {
			httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
		}
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");
		httpServletResponse.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
		httpServletResponse.setHeader("X-Powered-By","Jetty");
		 
		    String method= httpServletRequest.getMethod();
		    if (method.equals("OPTIONS")){
				  httpServletResponse.setStatus(200);
				  return false;
		    	}
		System.out.println(method);
		return true;
	  }
	 
	
	
	
	  public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
	  }
	 
	  public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
	  }

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */

	}
