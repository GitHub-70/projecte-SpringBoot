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

//	@Bean(name = "jasyptStringEncryptor2")
//	public StringEncryptor confingStringEncryptor() {
//		// PBE方式
//		PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();
//		
//		SimpleStringPBEConfig simpleStringPBEConfig = new SimpleStringPBEConfig();
//		simpleStringPBEConfig.setPassword("1212");
//		simpleStringPBEConfig.setAlgorithm("PBEWithMD5AndDES");
//		simpleStringPBEConfig.setKeyObtentionIterations("1000");
//		simpleStringPBEConfig.setPoolSize("1");
//		simpleStringPBEConfig.setProviderName("SunJCE");
//		simpleStringPBEConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
////		simpleStringPBEConfig.setIvGeneratorClassName("org.jasypt.salt.NoOpIVGenerator");
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
