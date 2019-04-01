package com.pengblog.test;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pengblog.captcha.CaptchaCodeGenerator;
import com.pengblog.redis.RedisUtil;
import com.pengblog.serviceInterface.IarticleService;

@RunWith(SpringJUnit4ClassRunner.class)//使用junit4进行测试  
@ContextConfiguration({"classpath:applicationContext.xml"})
public class Testt {

	@Autowired
	@Qualifier("articleService")
	private IarticleService articleService;
	
	@Test   //标明是测试方法  
	public void test() {  
		
		System.out.println(articleService.getCountOfArticle("article"));

     }  
	
}
