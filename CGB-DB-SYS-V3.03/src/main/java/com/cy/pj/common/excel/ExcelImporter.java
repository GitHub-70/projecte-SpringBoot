package com.cy.pj.common.excel;

import com.alibaba.excel.EasyExcel;
import com.cy.pj.common.listener.BatchExcelListener;

import java.io.File;

public class ExcelImporter {

    public static void importExcel(String filePath) {
        BatchExcelListener listener = new BatchExcelListener();
        EasyExcel.read(new File(filePath), listener).sheet().doRead();
    }

    public static void main(String[] args) {
//        String filePath = "E:\\file\\宁波银行外包商员工住址详情.xlsx";
        String filePath = "E:\\file\\aa.xlsx";
        try {
            importExcel(filePath);
        } catch (Exception e) {
            System.err.println("Error importing Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


