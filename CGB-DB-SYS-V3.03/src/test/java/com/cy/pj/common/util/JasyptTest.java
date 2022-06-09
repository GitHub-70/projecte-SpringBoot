package com.cy.pj.common.util;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JasyptTest {
 
    @Autowired
    private StringEncryptor encryptor;
    
    
    /**
     * 运行时加了启动参数：-Djasypt.encryptor.password=123456
     * 每次运行的加密结果不一样，但解密结果都一样
     */
    //加密
    @Test
    public void getPass(){
        String name = encryptor.encrypt("hello");
        System.out.println("加密结果："+name);//加密
        
    }
    
    @Test
    public void passDecrypt(){
        String username1 = encryptor.decrypt("w8Xhgf1p0AHOMsPB8yt4a4/CTiLFVGT5");
        String username2 = encryptor.decrypt("Ryy10wged6UW4q9ietusZSTrVuBtgR6d");
    	
        System.out.println("username1解密结果："+username1);
        System.out.println("username2解密结果："+username2);
    }
    
    @Test
    public void decrypt(){
    	/**
    	 * 此解密的启动参数秘钥：
    	 * 		-Djasypt.encryptor.password=bWVyY3VyeS1tYW5hZ2VyZnJhbWU=
    	 * 算法:PBEWithMD5AndDES
    	 * 
    	 * iv-generator-classname: org.jasypt.iv.NoIvGenerator
    	 * 
    	 * 注意与配置文件中的配置不一样
    	 */
    	
        String username3 = encryptor.decrypt("tUg8s24AQu/y5U2+g8mkpw==");
        System.out.println("username3解密结果："+username3);
    }
}