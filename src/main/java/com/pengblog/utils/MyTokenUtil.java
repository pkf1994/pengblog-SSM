
package com.pengblog.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.peng.exception.AuthenticationException;
import com.pengblog.bean.Administrator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class MyTokenUtil {
	

	public static String createJWT(long validityMillis, Administrator administrator) {
		//指定jwt签名算法
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		
		//生成JWT的时间
        Date now = new Date();
        
        //创建payload
        Map<String, Object> claims = new HashMap<String, Object>();
        
        claims.put("administrator_id", administrator.getAdministrator_id());
        claims.put("username", administrator.getAdministrator_username());
        // claims.put("administrator_password", administrator.getAdministrator_password());
        
        //生成签名的时候使用的秘钥secret
        String key = administrator.getAdministrator_password();
        
        //生成签发人
        String subject = administrator.getAdministrator_username();
        
        //下面就是在为payload添加各种标准声明和私有声明了
        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setSubject(subject)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, key);
        
        if (validityMillis >= 0) {
            long expMillis = now.getTime() + validityMillis;
            Date exp = new Date(expMillis);
            //设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
	}
	
	public static String createJWT(long validityMillis, String phoneNumber) {
		//指定jwt签名算法
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		
		//生成JWT的时间
        Date now = new Date();
        
        //创建payload
        Map<String, Object> claims = new HashMap<String, Object>();
        
        claims.put("phoneNumber", phoneNumber);
        
        //生成签名的时候使用的秘钥secret
        String key = getSigningKey(phoneNumber);
        
        //生成签发人
        String subject = phoneNumber;
        
        //下面就是在为payload添加各种标准声明和私有声明了
        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setSubject(subject)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, key);
        
        if (validityMillis >= 0) {
            long expMillis = now.getTime() + validityMillis;
            Date exp = new Date(expMillis);
            //设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
	}
	
	 public static Claims parseJWT(String token, Administrator administrator) {
		 
		 	//签名秘钥，和生成的签名的秘钥一模一样
	        String key = administrator.getAdministrator_password();

	        //得到DefaultJwtParser
	        Claims claims = Jwts.parser()
	                //设置签名的秘钥
	                .setSigningKey(key)
	                //设置需要解析的jwt
	                .parseClaimsJws(token).getBody();
	        
	        return claims;
	 }
	 
	 public static Claims parseJWT(String token, String phoneNumber) {
		 
		 	//签名秘钥，和生成的签名的秘钥一模一样
		 	String key = getSigningKey(phoneNumber);

	        //得到DefaultJwtParser
	        Claims claims = Jwts.parser()
	                //设置签名的秘钥
	                .setSigningKey(key)
	                //设置需要解析的jwt
	                .parseClaimsJws(token).getBody();
	        
	        return claims;
	 }
	 
	 public static Boolean isVerify(String token, Administrator administrator) throws AuthenticationException {
	        //签名秘钥，和生成的签名的秘钥一模一样
	        String key = administrator.getAdministrator_password();

	        //得到DefaultJwtParser
	        Claims claims = null;
	        
			try {
				claims = Jwts.parser()
						//设置签名的秘钥
						.setSigningKey(key)
						//设置需要解析的jwt
						.parseClaimsJws(token).getBody();
				
			} catch (ExpiredJwtException e) {
				
				throw new AuthenticationException("当前登录状态已失效");
				
			}


	        if (claims.get("administrator_password").equals(administrator.getAdministrator_password())) {
	            return true;
	        }

	        return false;
	    }
	 
	 public static Boolean isVerify(String token, String phoneNumber) throws AuthenticationException {
	        //签名秘钥，和生成的签名的秘钥一模一样
	        String key = getSigningKey(phoneNumber);

	        //得到DefaultJwtParser
	        Claims claims = null;
	        
			try {
				claims = Jwts.parser()
						//设置签名的秘钥
						.setSigningKey(key)
						//设置需要解析的jwt
						.parseClaimsJws(token).getBody();
				
			} catch (ExpiredJwtException e) {
				
				throw new AuthenticationException("当前登录状态已失效");
				
			}


	        if (claims.get("phoneNumber").equals(phoneNumber)) {
	            return true;
	        }

	        return false;
	    }
	 
	 	public static String getSigningKey(String phoneNumber) {
	 		
	 		String key = Md5Util.getMD5(phoneNumber).substring(0,5);
	 		
	 		return key;
	 		
	 	}
}
