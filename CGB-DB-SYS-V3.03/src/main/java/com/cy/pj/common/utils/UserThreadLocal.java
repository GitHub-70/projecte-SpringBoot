package com.cy.pj.common.utils;

import org.apache.shiro.authc.UsernamePasswordToken;


public class UserThreadLocal {

    //static不会影响影响线程  threadLocal创建时跟随线程.
    //private static ThreadLocal<Map<k,v>> threadLocal = new ThreadLocal<>();
    private static ThreadLocal<UsernamePasswordToken> threadLocal = new ThreadLocal<>();

    public static void set(UsernamePasswordToken usernamePasswordToken){

        threadLocal.set(usernamePasswordToken);
    }

    public static UsernamePasswordToken get(){

        return threadLocal.get();
    }

    public static void remove(){

        threadLocal.remove();
    }

}
