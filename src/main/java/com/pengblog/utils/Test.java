package com.pengblog.utils;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

public class Test {

	public static void main(String[] args) {
		
		System.out.println(md5Hex("ppaaxx_"));
		
	
	}
	
	private static String md5Hex(String src) {  
        try {  
            MessageDigest md5 = MessageDigest.getInstance("MD5");  
            byte[] bs = md5.digest(src.getBytes());  
            return new String(new Hex().encode(bs));  
        } catch (Exception e) {  
            return null;  
        }  
    }

	
}
