package com.pengblog.captcha;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pengblog.constant.PengblogConstant;

public class CaptchaCodeGenerator {
	
	
	private static Random random = new Random();

	private static char[] ch = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 
'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
 'j', 'k','l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

	
	public static String generate() {
		
		String captchaCode = "";
	    
	    //绘制字母数字
    	for (int i = 0; i < PengblogConstant.CAPTCHA_CODE_SIZE; i++){
	    	
    		captchaCode += ch[random.nextInt(ch.length)];
	       
	    }
    	
		return captchaCode;
	}
	
	public static String generate(int mySize) {
		
		String captchaCode = "";
	    
	    //绘制字母数字
    	for (int i = 0; i < mySize; i++){
	    	
    		captchaCode += ch[random.nextInt(ch.length)];
	       
	    }
    	
		return captchaCode;
	}


	
}