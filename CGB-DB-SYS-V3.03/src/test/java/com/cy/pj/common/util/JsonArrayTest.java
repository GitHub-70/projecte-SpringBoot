package com.cy.pj.common.util;

import org.json.JSONArray;
import org.json.JSONException;
/**
 * org.json.JSONArray
 * 		--不能转换 含有0开始的数据，否则会造成错误的数据
 * @author Administrator
 *
 */
public class JsonArrayTest {

	//准备数据
	static String jsonarr = "[{pid:023,name:离散,age:20300},{pid:024,name:喜禾,age:30200}]";
	
	public static void main(String[] args) throws JSONException {
		
		
		jsonConvertArray();
		
	}
	
	
	private static void jsonConvertArray() throws JSONException {
		JSONArray jsonArray = new JSONArray(jsonarr);
		
		System.out.println("jsonArray:--->"+jsonArray);
	}
}
