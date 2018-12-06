/**
 * 
 */
package com.pengblog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Administrator
 *
 */
public class ProcessInterceptor implements HandlerInterceptor {
	 
	  
	  public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		  
		/*
		httpServletResponse.setHeader("Content-Type", arg1);*/
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
