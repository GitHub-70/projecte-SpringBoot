package com.cy.pj.common.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 自定义加密配置类
 * @author Administrator
 *
 */
//@Component
public class EncryptorConfig implements StringEncryptor {

	/**
	 * 这里的配置与配置文件中的配置 重叠
	 */
//	@Bean(name = "jasyptStringEncryptor")
//	public StringEncryptor confingStringEncryptor() {
//		// PBE方式
//		PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();
//		
//		SimpleStringPBEConfig simpleStringPBEConfig = new SimpleStringPBEConfig();
//		simpleStringPBEConfig.setPassword("123456");
//		simpleStringPBEConfig.setAlgorithm("PBEWithMD5AndDES");
//		simpleStringPBEConfig.setKeyObtentionIterations("1000");
//		simpleStringPBEConfig.setPoolSize("1");
//		simpleStringPBEConfig.setProviderName("SunJCE");
//		simpleStringPBEConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
//		simpleStringPBEConfig.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
//		simpleStringPBEConfig.setStringOutputType("base64");
//		
//		
//		pooledPBEStringEncryptor.setConfig(simpleStringPBEConfig);
//		
//		return pooledPBEStringEncryptor;
//	}

	@Override
	public String encrypt(String message) {
		//自定义加密算法
		return message + "333";
	}

	@Override
	public String decrypt(String encryptedMessage) {
		//自定义解密算法
		return encryptedMessage.substring(0,encryptedMessage.indexOf("333"));
	}
	
}
