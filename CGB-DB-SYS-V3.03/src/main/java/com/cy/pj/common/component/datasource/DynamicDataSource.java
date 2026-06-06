package com.cy.pj.common.component.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 学习案例：AbstractRoutingDataSource — 读写分离数据源路由
 *
 * 知识点：
 *   AbstractRoutingDataSource 是 Spring 提供的数据源路由抽象类：
 *   - 它维护一个"目标数据源 Map"（key=数据源标识，value=实际 DataSource）
 *   - 每次获取连接时调用 determineCurrentLookupKey() 得到当前应使用的数据源标识
 *   - 根据标识从 Map 中取出对应的 DataSource 并返回
 *
 *   配合 DataSourceContextHolder + RoutingDataSourceProcessor，完整链路为：
 *   请求 → @ReadOnly注解 → 代理设置ThreadLocal → AbstractRoutingDataSource路由 → 目标数据源
 *
 * ⚠️ 本类未注册为 @Component，需在配置类中手动创建 Bean 并注入读写数据源才能生效。
 *   示例配置：
 *   @Bean
 *   public DataSource dataSource() {
 *       DynamicDataSource ds = new DynamicDataSource();
 *       Map<Object, Object> targetDataSources = new HashMap<>();
 *       targetDataSources.put("write", writeDataSource());
 *       targetDataSources.put("read", readDataSource());
 *       ds.setTargetDataSources(targetDataSources);
 *       ds.setDefaultTargetDataSource(writeDataSource()); // 默认使用写库
 *       return ds;
 *   }
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // 从 ThreadLocal 获取当前线程标记的数据源类型
        // 如果为 null（未标记），AbstractRoutingDataSource 会回退到 defaultTargetDataSource
        String dataSourceType = DataSourceContextHolder.getDataSourceType();
        return dataSourceType;
    }

    /**
     * 初始化时设置目标数据源映射
     * @param targetDataSources key=数据源标识（如 "read"/"write"），value=对应的 DataSource 实例
     * @param defaultDataSource 默认数据源（通常为写库）
     */
    public void configure(Map<Object, Object> targetDataSources, DataSource defaultDataSource) {
        this.setTargetDataSources(targetDataSources);
        this.setDefaultTargetDataSource(defaultDataSource);
        // afterPropertiesSet() 必须调用，它会解析 targetDataSources Map 并建立查找索引
        this.afterPropertiesSet();
    }
}
