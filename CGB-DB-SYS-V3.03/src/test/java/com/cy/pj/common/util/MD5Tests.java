package com.cy.pj.common.util;

import java.util.UUID;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

/**
 * MD5（Message-Digest Algorithm 5）是一种广泛使用的哈希函数，它可以将任意长度的数据转换成
 * 一个固定长度（128位或16字节）的哈希值。MD5最初设计用于数据完整性检查，例如文件校验
 * MD5存在以下问题：
 * 彩虹表攻击：
 * 攻击者可以预先计算出大量常见密码的MD5哈希值，并将这些哈希值存储在所谓的“彩虹表”中。
 * 当获取到一个哈希值时，攻击者可以在彩虹表中查找对应的明文密码，这大大降低了破解密码所需的时间。
 * 碰撞攻击：
 * MD5容易受到碰撞攻击，即两个不同的输入产生相同的哈希值。
 * 这种攻击可以通过特定算法和计算资源来实现，对于密码存储来说尤其危险。
 *
 */
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

	/**
	 * MD5算法已经被证明容易受到碰撞攻击，即使使用盐值和多次散列，也不能完全消除这种风险
	 * 虽然使用MD5、盐值和增加散列次数比仅使用MD5更安全，但这仍然不是最推荐的做法。
	 * 对于密码散列，更安全的选择是使用现代的密码散列函数，如bcrypt、scrypt或Argon2。这些算法具有以下优势：
	 * 		内置盐值：这些算法自动加入随机盐值。
	 * 		可调节的工作因子：允许你调整散列过程的复杂度，从而增加攻击者的成本。
	 * 		内存消耗：某些算法（如scrypt和Argon2）利用了大量的内存，这使得暴力攻击更加困难。
	 */
//	@Test
//	void testBCrypt() {
//		String password = "mySecurePassword";
//		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12)); // 12是工作因子，可
//		System.out.println("Hashed Password: " + hashedPassword);
//	}
	  
}





