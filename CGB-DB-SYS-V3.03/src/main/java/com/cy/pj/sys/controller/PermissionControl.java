package com.cy.pj.sys.controller;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.HashMap;
import java.util.Map;

/**
 * 利用Aviator表达式引擎实现权限控制
 *      Java函数实现了一个简单的权限控制功能
 */
public class PermissionControl {

    public static void main(String[] args) {
        // 为了保持示例的简洁性，规则和用户数据仍然被硬编码，但在实际应用中，应确保它们的安全性
        String rule = "mapGet(hashMap, 'role') == 'admin' && startsWith(mapGet(hashMap, 'path'), '/admin')";
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("role", "admin");
        hashMap.put("path", "/admin/dashboard");

        // 首先注册自定义函数mapGet，确保在执行规则之前可用
        registerCustomFunctions();

        try {
            boolean hasAccess = evaluateRule(rule, hashMap);
            if (hasAccess) {
                System.out.println("用户有权限访问该路径");
            } else {
                System.out.println("用户无权限访问该路径");
            }
        } catch (Exception e) {
            System.err.println("规则评估过程中出现错误: " + e.getMessage());
        }
    }

    private static boolean evaluateRule(String rule, Map<String, Object> data) throws Exception {
        // 使用AviatorEvaluator执行规则
        Object result = AviatorEvaluator.execute(rule, data, false);
        // 强制转换结果前进行类型检查
        if (!(result instanceof Boolean)) {
            throw new Exception("规则表达式结果不是布尔类型");
        }
        return (boolean) result;
    }

    private static void registerCustomFunctions() {
        // 更新自定义函数mapGet以接受两个参数：Map和 key
        AviatorEvaluator.addFunction(new AbstractFunction() {

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                // 确保arg1是Map类型，arg2是String类型,由 规则表达式 决定
                String key = FunctionUtils.getStringValue(arg2, env);
                return new AviatorRuntimeJavaType(env.get(key)); // 返回找到的值
            }

            // 自定义函数的名称
            @Override
            public String getName() {
                return "mapGet";
            }

        });

        // 更新自定义函数startsWith以接受两个参数：String和prefix
        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject stringObj, AviatorObject prefixObj) {
                // 确保arg1是String类型，arg2是String类型,由 规则表达式 决定
                String value = FunctionUtils.getStringValue(stringObj, env);
                String prefix = FunctionUtils.getStringValue(prefixObj, env);
//                return new AviatorBoolean.valueOf(value.startsWith(prefix)); // 由此方法，但不知为何报错
                return new AviatorRuntimeJavaType(value.startsWith(prefix));
            }

            // 自定义函数的名称
            @Override
            public String getName() {
                return "startsWith";
            }
        });
    }

}



