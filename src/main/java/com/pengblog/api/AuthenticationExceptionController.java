package com.pengblog.api;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import com.peng.exception.AuthenticationException;

@RestController
@ControllerAdvice
public class AuthenticationExceptionController {
	
	@ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
	public Object handleAuthenticationException(AuthenticationException ae) {
        
        return ae.getMessage();
		
	}
	

	@ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
	public Object handleCommonException(Exception e) {
		
        return e.getMessage();
		
	}
	
	

}
