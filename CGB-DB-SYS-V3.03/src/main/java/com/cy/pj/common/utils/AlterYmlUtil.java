package com.cy.pj.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AlterYmlUtil {

	/**
	 * yml文件的每一行
	 */
	private List<String> lineList = new ArrayList<>();
	
	/**
	 * 缩进的字符长度
	 */
	private Integer preLen;
	
	/**
	 * 缩进的字符
	 */
	private String preStr;
	

	public List<String> getLineList() {
		return lineList;
	}

	public void setLineList(List<String> lineList) {
		this.lineList = lineList;
	}

	public Integer getPreLen() {
		return preLen;
	}

	public void setPreLen(Integer preLen) {
		this.preLen = preLen;
	}

	public String getPreStr() {
		return preStr;
	}

	public void setPreStr(String preStr) {
		this.preStr = preStr;
	}
	
	
	/**
	 * 利用构造函数，初始化属性
	 * @param inputStream
	 * @throws IOException
	 */
	public AlterYmlUtil(InputStream inputStream) throws IOException {
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String line;
		while(null != (line = bufferedReader.readLine())) {
			this.lineList.add(line);
		}
		// 获取第一行数据
		char[] charArray = lineList.get(1).toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			
			// 实例化 preLen 属性
			// charArray[i] + ""转换成 CharSequence对象
			if (Pattern.compile("[a-zA-z]").matcher(charArray[i] + "").matches()) {
				this.preLen = i;
				break;
			}
		}
		// 实例化 preStr 属性
		this.preStr = this.lineList.get(1).substring(0, this.preLen);
	}
	
	
}
