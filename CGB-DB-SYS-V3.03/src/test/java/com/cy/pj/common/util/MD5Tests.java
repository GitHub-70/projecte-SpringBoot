package com.cy.pj.common.util;

import java.util.UUID;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
public class MD5Tests {
      //MD5算法是一种消息摘要算法,其特点
	  //1)加密不可逆
	  //2)相同内容加密结果也相同
	  @Test
	  void testMD501() {
		  String password="123456";
		  //基于MD5算法进行密码加密
		  String hashedPwd=DigestUtils.md5DigestAsHex(password.getBytes());
		  System.out.println(hashedPwd);//e10adc3949ba59abbe56e057f20f883e
		                                //e10adc3949ba59abbe56e057f20f883e
		                                //e10adc3949ba59abbe56e057f20f883e
	  }
	  @Test
	  void testMD502() {
		  String password="123456";
		  String salt=UUID.randomUUID().toString();//产生一个随机字符串
		  System.out.println("salt="+salt);
		  //基于MD5算法进行密码加密
		  String hashedPwd=DigestUtils.md5DigestAsHex((salt+password).getBytes());
		  System.out.println("hashedPwd="+hashedPwd);
	  }
	  @Test
	  void testMD503() {
		  String password="123456";
		  String salt=UUID.randomUUID().toString();//产生一个随机字符串
		  SimpleHash sh=new SimpleHash("MD5", password, salt, 1024);//1024为加密次数
		  String hashedPwd=sh.toHex();
		  System.out.println(hashedPwd);//37b6af0d982e68bf96b3a98c51ac78a1
	  }
	  
}





