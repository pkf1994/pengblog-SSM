package com.pengblog.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;

public class Md5Util {
	
	public static String getMD5(String input) {
		try {  
            MessageDigest md5 = MessageDigest.getInstance("MD5");  
            byte[] bs = md5.digest(input.getBytes());  
            return new String(new Hex().encode(bs));  
        } catch (Exception e) {  
            return null;  
        }  
	}
	
	public static String getSalt(Date saltDate) {
		
		String salt = new SimpleDateFormat("ssmmhhddMMyyyy").format(saltDate);
		
		return salt;
	}
	
	public static Boolean verify(String inputPassword, String dbPassword) {
		
		if(inputPassword.equals(dbPassword)) {
			return true;
		}
		
		return false;
		
	}
}
