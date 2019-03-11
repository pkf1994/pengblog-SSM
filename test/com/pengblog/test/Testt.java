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

@RunWith(SpringJUnit4ClassRunner.class)//使用junit4进行测试  
@ContextConfiguration({"classpath:applicationContext.xml"})
public class Testt {

	@Test   //标明是测试方法  
	public void test() {  
		 // 创建相框
        JFrame jFrame = new JFrame();
        // 创建画板
        JPanel jpanel = new JPanel() {
            //序列号（可省略）
            private static final long serialVersionUID = 1L;

            // 重写paint方法
            @Override
            public void paint(Graphics g) {
            	
            	Random random = new Random();
            	
            	int width = 400;
            	int height = 150;
            	int size = 5;
                // 必须先调用父类的paint方法
                super.paint(g);
                
                //填充白色背景
        	    g.setColor(Color.WHITE);
        	    g.fillRect(0, 0, width, height);
        	    
        	    
        	    //绘制字母数字
            	for (int i = 0; i < size; i++){
            		
            		char[] ch = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 
            				'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            				 'j', 'k','l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        	    	
        	        String c = "" + ch[random.nextInt(ch.length)];
        	        
        	        //设置字体高度为图片高度的80%
        	        int fontSize = (int)(height * 0.8);
        	        
        	        int fx = (width/size) * i;
        	        
        	        int fy = 0;
        	        
        	        
        	        // 设置字体颜色
                    g.setColor(CaptchaCodeGenerator.getRandomColor());
                    
        	        g.drawString(c,fx,fy);
        	    }
            }
        };
        //将绘有小人图像的画板嵌入到相框中
        jFrame.add(jpanel);
        // 设置画框大小（宽度，高度），默认都为0
        jFrame.setSize(400, 150);
        // 将画框展示出来。true设置可见，默认为false隐藏
        jFrame.setVisible(true);
        
        try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

     }  
	
}
