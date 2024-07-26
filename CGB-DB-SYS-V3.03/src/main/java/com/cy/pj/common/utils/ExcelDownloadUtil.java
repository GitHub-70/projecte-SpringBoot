package com.cy.pj.common.utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ExcelDownloadUtil {

    public static void downloadExcel(HttpServletResponse response, List<Map<String, Object>> data, String[] headers, String mergedHeader) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=export.xlsx");

        try (OutputStream out = response.getOutputStream();
             SXSSFWorkbook workbook = new SXSSFWorkbook(100)) { // 保留100行数据在内存中

            // 创建工作表
            Sheet sheet = workbook.createSheet("Sheet1");

            // 创建合并的表头行
            Row headerRow = sheet.createRow(0);
            Cell mergedCell = headerRow.createCell(0);
            mergedCell.setCellValue(mergedHeader);

            // 合并单元格
            CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, headers.length - 1);
            sheet.addMergedRegion(mergedRegion);

            // 创建标题行
            Row titleRow = sheet.createRow(1);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 填充数据行
            int rowNum = 2;
            for (Map<String, Object> rowData : data) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (String header : headers) {
                    Cell cell = row.createCell(colNum++);
                    Object value = rowData.get(header);
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入输出流
            workbook.write(out);
        }
    }


    public static void downloadExcel2(HttpServletResponse response, List<Object[]> data, String[] headers, String mergedHeader) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=export.xlsx");

        try (OutputStream out = response.getOutputStream();
             SXSSFWorkbook workbook = new SXSSFWorkbook(1000)) { // 保留1000行数据在内存中

            // 创建工作表
            Sheet sheet = workbook.createSheet("Sheet1");

            // 创建合并的表头样式
            XSSFCellStyle mergedHeaderStyle = (XSSFCellStyle) workbook.createCellStyle();
            mergedHeaderStyle.setAlignment(HorizontalAlignment.CENTER); // 设置水平居中

            // 创建合并的表头行
            Row headerRow = sheet.createRow(0);
            Cell mergedCell = headerRow.createCell(0);
            mergedCell.setCellValue(mergedHeader);
            mergedCell.setCellStyle(mergedHeaderStyle); // 应用样式


            // 合并单元格
            CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, headers.length - 1);
            sheet.addMergedRegion(mergedRegion);

            // 创建标题行
            Row titleRow = sheet.createRow(1);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 填充数据行
            int rowNum = 2;
            for (Object[] rowData : data) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (Object value : rowData) {
                    Cell cell = row.createCell(colNum++);
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }

            // 自动调整列宽
//            for (int i = 0; i < headers.length; i++) {
//                sheet.autoSizeColumn(i);
//            }

            // 写入输出流
            workbook.write(out);
        }
    }
}


