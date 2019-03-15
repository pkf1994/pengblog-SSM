package com.pengblog.test;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pengblog.captcha.CaptchaCodeGenerator;
import com.pengblog.redis.RedisUtil;

@RunWith(SpringJUnit4ClassRunner.class)//使用junit4进行测试  
@ContextConfiguration({"classpath:applicationContext.xml"})
public class Testt {

	@Test   //标明是测试方法  
	public void test() {  
		
		RedisUtil.setStringKV("13660274192", "qwee", 2*60, 2);

     }  
	
}
