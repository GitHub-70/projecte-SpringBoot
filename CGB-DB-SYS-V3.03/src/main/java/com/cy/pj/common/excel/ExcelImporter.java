package com.cy.pj.common.excel;

import com.cy.pj.common.utils.ExcelDocmentUtil;
import com.cy.pj.sys.service.excel.ExcelDataDealSevice;

import java.util.List;
import java.util.Map;

/**
 * 模拟个性化 Excel导入数据处理逻辑
 * @author Administrator
 * 单继承，多实现
 *      如果出现了继承多个类，则这多个类是父子关系（实际也是单继承），不符合最佳实践
 */
public class ExcelImporter implements ExcelDataDealSevice {

    @Override
    public void excelDataDoSomething(List<Map<String, Object>> dataList) {
        System.out.println("开始处理数据...");
        for (Map<String, Object> data : dataList) {
            System.out.println(data);
        }
    }



    public static void main(String[] args) {
//        String filePath = "E:\\file\\宁波银行外包商员工住址详情.xlsx";
        String filePath = "E:\\file\\aa.xlsx";
        try {
            ExcelDocmentUtil.importExcel(filePath, new ExcelImporter());
        } catch (Exception e) {
            System.err.println("Error importing Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


