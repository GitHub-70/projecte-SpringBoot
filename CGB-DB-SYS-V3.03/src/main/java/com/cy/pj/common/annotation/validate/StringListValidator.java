package com.cy.pj.common.annotation.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义注解校验器
 *     用于验证参数是否在指定的参数列表中
 *
 *     优先于 @NotBlank 执行
 * @author
 * @create 2022-03-09 16:05
 * @description
 */
public class StringListValidator implements ConstraintValidator<StringList, String> {

    private Set<String> params = new HashSet<>();
    /**
     * 初始化方法
     *   将所有参数放入set集合中
     * @param constraintAnnotation
     */
    @Override
    public void initialize(StringList constraintAnnotation) {
        String[] values = constraintAnnotation.values();
        for (String value : values) {
            params.add(value);
        }
    }

    /**
     * 判断是否合法
     * @param val
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(String val, ConstraintValidatorContext constraintValidatorContext) {
        if (params.contains(val)) {
            return true;
        }
        return false;
    }
}
