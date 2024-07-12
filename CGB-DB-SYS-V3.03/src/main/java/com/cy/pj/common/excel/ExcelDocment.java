package com.cy.pj.common.excel;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.SheetBuilder;

/**
 * 导出excel
 * https://blog.csdn.net/sufu1065/article/details/115301974
 */
public class ExcelDocment {
	// 显示导出表的标题
	private static String title = "工作簿中一个标签页名字";
	// 导出表的列名
	private static String[] rowsName;
	// 查询到的数据
	private static List<Object[]> dataList = new ArrayList<>();

	// 创建一个工作簿
	private static HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
	// 创建一个标签页
	private static HSSFSheet hssfSheet = hssfWorkbook.createSheet(title);
	// 标签页的标题行
	private static HSSFRow hssfRow = hssfSheet.createRow(0);// 第一行
	// 单元格
	private static HSSFCell hssfCell = hssfRow.createCell(0); // 第一行中的第一个单元格

	public static void main(String[] args) {
		rowsName = new String[] { "序号", "姓名", "年龄", "性别", "地址" };
		String[] data = {"张三", "男", "18", "北京", "北京"};
		String[] data2 = {"李四", "女", "18", "北京", "北京"};
		dataList.add(data);
		dataList.add(data2);

		 // 获取一个工作簿 模板
		 HSSFWorkbook workTemplet = getWorkTemplet(hssfWorkbook, hssfSheet, hssfRow, hssfCell);
		
		// 将查询到数据设置到对应的sheet表格中
		 for (int i = 0; i < dataList.size(); i++) {
			 Object[] dateRows = dataList.get(i);
			 // 根据行号 创建所需要的行
			 HSSFRow dataRow = hssfSheet.createRow(i + 3);
			 
			 for (int j = 0; j < dateRows.length; j++) {
				HSSFCell hssCell = null;
				// 当第一个单元格时，填充序号
				if(j == 0) {
					hssCell = dataRow.createCell(j, CellType.NUMERIC);
					hssCell.setCellValue(i+1);// 序号从1 开始
					
				} else {
					hssCell = dataRow.createCell(j, CellType.STRING);
					if (null != dateRows[j] && !"".equals(dateRows[j]) ) {
						// 填充数据
						hssCell.setCellValue(dateRows[j].toString());
					}
				}
				// 设置创建的单元格风格
				hssCell.setCellStyle(getDataStyle(workTemplet));
			}
		}

		 try(FileOutputStream fileOutputStream = new FileOutputStream("D:\\test.xls");){
			 workTemplet.write(fileOutputStream);
			 workTemplet.close();
		 } catch (Exception e){
			 e.printStackTrace();
		 }
		 System.out.println("导出成功");
	}

	/**
	 * 获取一个工作簿模板
	 */
	private static HSSFWorkbook getWorkTemplet(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, HSSFRow hssfRow, HSSFCell hssfCell) {

		// 定义sheet样式
		HSSFCellStyle tableHeadStyle = getTableHeadStyle(hssfWorkbook);
		HSSFCellStyle dataStyle = getTableHeadStyle(hssfWorkbook);
		// CellRangeAddress--参数：起始行号，终止行号，起始列号，终止列号
		hssfSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowsName.length - 1)));// 合并第一行
		hssfCell.setCellStyle(tableHeadStyle);
		hssfCell.setCellValue(title);

		// 定义所需要的列数 表头行号
		int columnNum = rowsName.length;
		HSSFRow hRowName = hssfSheet.createRow(2);

		// 将表头设置到sheet的单元格中
		for (int i = 0; i < columnNum; i++) {
			// 创建columnNum个单元格
			HSSFCell hCell = hRowName.createCell(i);
			// 设置单元格类型
			hCell.setCellType(CellType.STRING);
			HSSFRichTextString hssfRichTextStringVale = new HSSFRichTextString(rowsName[i]);
			// 设置表头 值与风格
			hCell.setCellValue(hssfRichTextStringVale);
			hCell.setCellStyle(tableHeadStyle);
		}
		return hssfWorkbook;
	}

	/**
	 * 表头单元格样式
	 * 
	 * @param hssfWorkbook
	 * @return
	 */
	private static HSSFCellStyle getTableHeadStyle(HSSFWorkbook hssfWorkbook) {
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
	
	
	private static HSSFCellStyle getDataStyle(HSSFWorkbook hssfWorkbook) {
		// 设置字体
		HSSFFont hssfFont = hssfWorkbook.createFont();

		// 设置字体大小
		hssfFont.setFontHeightInPoints((short) 8);
		// 字体不加粗
		hssfFont.setBold(false);
		// 设置名字
		hssfFont.setFontName("字体名字：软黑");

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
