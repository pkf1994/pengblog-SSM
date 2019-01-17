package com.pengblog.api;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import com.peng.exception.AuthenticationException;
import com.pengblog.bean.ErrorInfo;

@RestController
@ControllerAdvice
public class AuthenticationExceptionController {
	
	@ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
	public ErrorInfo handleAuthenticationException(AuthenticationException ae) {
		
		ErrorInfo r = new ErrorInfo();
        r.setSuccess(false);
        r.setMsg(ae.getMessage());
        
        return r;
		
	}
	

	@ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
	public ErrorInfo handleCommonException(Exception e) {
		
		ErrorInfo r = new ErrorInfo();
        r.setSuccess(false);
        r.setMsg(e.getMessage());
        
        return r;
		
	}
	
	

}
