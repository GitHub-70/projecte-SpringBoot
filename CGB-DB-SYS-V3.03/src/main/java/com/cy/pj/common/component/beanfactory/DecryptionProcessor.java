package com.cy.pj.common.component.beanfactory;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 学习案例：BeanFactoryPostProcessor — 加密属性解密
 *
 * 知识点：
 *   在配置文件中使用加密值（如 ENC(...)）是保护敏感信息的常见做法。
 *   BeanFactoryPostProcessor 可以在 Bean 实例化前扫描所有属性值，识别加密标记并解密。
 *
 * ⚠️ 本案例已被禁用（无 @Component），因为：
 *   项目已引入 jasypt-spring-boot-starter（3.0.3），Jasypt 会自动识别 ENC() 标记并解密，
 *   且支持多种算法（PBEWithMD5AndDES、PBEWITHHMACSHA512ANDAES_256 等）。
 *   手动实现解密处理器会与 Jasypt 冲突，且此简化实现不符合 Jasypt 的加密规范。
 *
 * 如需测试：临时加上 @Component，但需同时禁用 Jasypt 的自动配置
 *           （@SpringBootApplication(exclude = JasyptAutoConfiguration.class)）。
 */
public class DecryptionProcessor implements BeanFactoryPostProcessor {

    private StringEncryptor encryptor;

    /**
     * 注入 Jasypt 的 StringEncryptor 实例，保证解密算法与加密算法一致
     */
    public void setEncryptor(StringEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            processEncryptedProperties(beanDefinition);
        }
    }

    private void processEncryptedProperties(BeanDefinition beanDefinition) {
        MutablePropertyValues properties = beanDefinition.getPropertyValues();
        for (PropertyValue property : properties.getPropertyValues()) {
            Object value = property.getValue();
            if (value instanceof String && isEncrypted((String) value)) {
                String decryptedValue = decrypt((String) value);
                properties.add(property.getName(), decryptedValue);
            }
        }
    }

    private boolean isEncrypted(String value) {
        return value != null && value.startsWith("ENC(") && value.endsWith(")");
    }

    /**
     * 解密方法：委托给 Jasypt 的 StringEncryptor
     *
     * Jasypt 的 ENC() 格式为：ENC(密文)
     *   - 密文是经过 PBE 算法加密后的 Base64 编码字符串
     *   - StringEncryptor.decrypt() 会自动处理 Base64 解码和 PBE 解密
     *
     * 注意：如果使用自定义解密（非 Jasypt），需要自行实现 AES/DES 等算法，
     *       且需确保加密时使用的密钥、算法、IV 等参数与解密时完全一致。
     */
    private String decrypt(String encryptedValue) {
        if (encryptor != null) {
            // 使用 Jasypt 的标准解密：直接传入密文部分，无需手动截取 ENC()
            // Jasypt 内部的 EncryptablePropertyResolver 会处理 ENC() 前缀
            String encryptedContent = encryptedValue.substring(4, encryptedValue.length() - 1);
            return encryptor.decrypt(encryptedContent);
        }
        // 无 encryptor 时，简单示例：仅截取密文内容（不可用于生产！）
        return encryptedValue.substring(4, encryptedValue.length() - 1);
    }
}
