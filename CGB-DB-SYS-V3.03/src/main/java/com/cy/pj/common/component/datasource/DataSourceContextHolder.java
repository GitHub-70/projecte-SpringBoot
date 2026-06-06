package com.cy.pj.common.component.datasource;

/**
 * 数据源上下文持有者 — 读写分离路由的 ThreadLocal 上下文
 *
 * 知识点：
 *   ThreadLocal 为每个线程维护独立的变量副本，确保线程间数据隔离。
 *   在读写分离场景中，业务线程通过 setDataSourceType("read"/"write") 标记当前操作类型，
 *   DynamicDataSource（AbstractRoutingDataSource）在获取连接时通过 getDataSourceType()
 *   决定使用哪个数据源。
 *
 *   使用模式：
 *   1. Repository 方法上标注 @ReadOnly → RoutingDataSourceProcessor 代理拦截 → setDataSourceType("read")
 *   2. DynamicDataSource.determineCurrentLookupKey() → getDataSourceType() → 返回 "read" 数据源
 *   3. 方法执行完毕 → finally 中 clear() 清除 ThreadLocal，避免线程复用时数据源泄漏
 *
 * ⚠️ 当前项目中缺少 DynamicDataSource（AbstractRoutingDataSource 实现），
 *   因此此 ContextHolder 被设置后未被消费。配合 DynamicDataSource 使用才能真正生效。
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static String getDataSourceType() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
