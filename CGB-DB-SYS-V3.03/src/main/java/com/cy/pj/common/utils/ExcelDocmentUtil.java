package com.cy.pj.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Objects;

/**
 * Excel文档工具类
 *      HSSF方式：这种方式导出的文件格式为office 2003专用格式，即.xls，优点是导出数据速度快，但是最多65536行数据
 *      XSSF方式：这种方式导出的文件格式为office 2007专用格式，即.xlsx，优点是导出的数据不受行数限制，缺点导出速度慢
 *      SXSSF方式：SXSSF 是 XSSF API的兼容流式扩展，主要解决当使用 XSSF 方式导出大数据量时，内存溢出的问题，支持导出大批量的excel数据
 *               主要特性是低内存，导出的时候，先将数据写入磁盘再导出，避免报内存不足
 * @author cy
 */
public class ExcelDocmentUtil {

    /**
     * 获取一个工作簿模板
     */
    public static HSSFWorkbook getWorkTemplet(HSSFWorkbook hssfWorkbook,
                                              String[] rowsName,
                                              List<Object[]> dataRows,
                                              String title) {
        HSSFSheet hssfSheet = hssfWorkbook.createSheet(title); // 创建一个标签页
        HSSFRow hssfRow = hssfSheet.createRow(0);// 第一行
        HSSFCell hssfCell = hssfRow.createCell(0);// 第一行中的第一个单元格
        // 定义sheet样式
        HSSFCellStyle tableHeadStyle = getTableHeadStyle(hssfWorkbook);
        HSSFCellStyle dataStyle = getTableHeadStyle(hssfWorkbook);
        // CellRangeAddress--参数：起始行号，终止行号，起始列号，终止列号
        hssfSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, rowsName.length));// 合并第一行
        hssfCell.setCellStyle(tableHeadStyle);
        hssfCell.setCellValue(title);

        // 定义所需要的列数 表头行号
        HSSFRow hRowName = hssfSheet.createRow(2);
        // 将表头设置到sheet的单元格中
        for (int i = 0; i < rowsName.length; i++) {
            // 创建columnNum个单元格
            HSSFCell hCell = hRowName.createCell(i+1); // 第一列为序号，从第二列开始
            // 设置单元格类型
            hCell.setCellType(CellType.STRING);
            HSSFRichTextString hssfRichTextStringVale = new HSSFRichTextString(rowsName[i]);
            // 设置表头 值与风格
            hCell.setCellValue(hssfRichTextStringVale);
            hCell.setCellStyle(tableHeadStyle);
        }
        // 将查询到数据设置到对应的sheet表格中
        for (int i = 0; i < dataRows.size(); i++) {
            Object[] dateRows = dataRows.get(i);
            // 根据行号 创建所需要的行
            HSSFRow dataRow = hssfSheet.createRow(i + 3);

            for (int j = 0; j < dateRows.length+1; j++) {
                HSSFCell hssCell = null;
                // 当第一个单元格时，填充序号
                if(j == 0) {
                    hssCell = dataRow.createCell(j, CellType.NUMERIC);
                    hssCell.setCellValue(i+1);// 序号从1 开始

                } else {
                    hssCell = dataRow.createCell(j, CellType.STRING);
                     // 填充数据
                    hssCell.setCellValue(Objects.toString(dateRows[j-1],""));
                }
                // 设置创建的单元格风格
                hssCell.setCellStyle(getDataStyle(hssfWorkbook));
            }
        }
        return hssfWorkbook;
    }

    /**
     * 表头单元格样式
     *
     * @param hssfWorkbook
     * @return
     */
    public static HSSFCellStyle getTableHeadStyle(HSSFWorkbook hssfWorkbook) {
        // 设置字体
        HSSFFont hssfFont = hssfWorkbook.createFont();

        // 设置字体大小
        hssfFont.setFontHeightInPoints((short) 11);
        // 字体加粗
        hssfFont.setBold(true);
        // 设置名字
        hssfFont.setFontName("字体名字：软黑");

        // 设置样式
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        // 设置底边框
        hssfCellStyle.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色
        hssfCellStyle.setBottomBorderColor((short) 5);
        // 设置右边框
        hssfCellStyle.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色
        hssfCellStyle.setRightBorderColor((short) 4);
        // 设置顶边框
        hssfCellStyle.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色
        hssfCellStyle.setTopBorderColor((short) 3);

        // 在样式中 设置应用的字体
        hssfCellStyle.setFont(hssfFont);
        // 设置自动换行
        hssfCellStyle.setWrapText(false);
        // 设置对齐的样式 为 向上 居中对齐
        hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
        hssfCellStyle.setVerticalAlignment(VerticalAlignment.TOP);

        return hssfCellStyle;
    }


    public static HSSFCellStyle getDataStyle(HSSFWorkbook hssfWorkbook) {
        // 设置字体
        HSSFFont hssfFont = hssfWorkbook.createFont();

        // 设置字体大小
        hssfFont.setFontHeightInPoints((short) 8);
        // 字体不加粗
        hssfFont.setBold(false);
        // 设置名字
        hssfFont.setFontName("宋体");

        // 设置样式
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        // 设置底边框
        hssfCellStyle.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色
        hssfCellStyle.setBottomBorderColor((short) 4);
        // 设置右边框
        hssfCellStyle.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色
        hssfCellStyle.setRightBorderColor((short) 4);
        // 设置顶边框
        hssfCellStyle.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色
        hssfCellStyle.setTopBorderColor((short) 4);

        // 在样式中 设置应用的字体
        hssfCellStyle.setFont(hssfFont);
        // 设置自动换行
        hssfCellStyle.setWrapText(false);
        // 设置对齐的样式 为 水平 居中对齐
        hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
        hssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return hssfCellStyle;
    }
}
