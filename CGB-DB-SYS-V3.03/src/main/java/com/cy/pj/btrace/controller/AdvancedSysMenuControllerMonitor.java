package com.cy.pj.btrace.controller;

import org.openjdk.btrace.core.types.AnyType;
import org.openjdk.btrace.core.BTraceUtils;
import org.openjdk.btrace.core.annotations.*;

@BTrace
public class AdvancedSysMenuControllerMonitor {

    @TLS
    static long beginTime;

    @OnMethod(
            clazz = "com.cy.pj.sys.controller.SysMenuController",
            method = "/do.*Object/"
    )
    public static void traceExecute(@ProbeMethodName String methodName, AnyType[] args) {
        beginTime = BTraceUtils.timeMillis();
        BTraceUtils.println("=====================================");
        BTraceUtils.println("Method: " + methodName);
        BTraceUtils.println("Start time: " + beginTime);
        BTraceUtils.print("Arguments: ");
        BTraceUtils.printArray(args);
        BTraceUtils.println("");
    }

    @OnMethod(
            clazz = "com.cy.pj.sys.controller.SysMenuController",
            method = "/do.*Object/",
            location = @Location(Kind.RETURN)
    )
    public static void traceReturn(@ProbeMethodName String methodName, @Duration long duration) {
        long endTime = BTraceUtils.timeMillis();
        BTraceUtils.println("End time: " + endTime);
        BTraceUtils.println("Duration: " + duration + " ns (" + (duration/1000000) + " ms)");
        BTraceUtils.println("=====================================");
    }
}