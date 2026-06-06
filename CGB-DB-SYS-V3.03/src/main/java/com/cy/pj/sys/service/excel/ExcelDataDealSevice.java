package com.cy.pj.sys.service.excel;

import java.util.List;
import java.util.Map;

/**
 * excel数据处理基础服务
 */
public interface ExcelDataDealSevice {


    /**
     * excel数据处理
     * @param dataList
     */
    default void excelDataDoSomething(List<Map<String, Object>> dataList){
        for (Map<String, Object> data : dataList) {
            System.out.println(data);
        }
    };
}
