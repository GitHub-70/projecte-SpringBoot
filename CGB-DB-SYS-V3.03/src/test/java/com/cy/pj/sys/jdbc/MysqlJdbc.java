package com.cy.pj.sys.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MysqlJdbc {
	
	public int getJdbc() {
		
		try {
			//1. 注册数据库驱动
			Class.forName("com.mysql.jdbc.Driver");
			//2.定义url/username/password/sql
			String url = "jdbc:mysql://192.168.189.1:3306/dbms?serverTimezone=GMT%2B8&characterEncoding=utf8";
			String username = "root";
			String password = "root";
			String sql = "";
			//3. 获取连接
			Connection connection = DriverManager.getConnection(url, username, password);
			//4. 获取statement对象
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			//5. 执行sql
			boolean execute = prepareStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

}
