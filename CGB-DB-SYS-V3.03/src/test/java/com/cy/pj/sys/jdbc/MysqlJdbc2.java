package com.cy.pj.sys.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class MysqlJdbc2 {
	
	/**
	 *  以insert table values(...),(...),(...)形式插入
	 *  注意堆内存溢出
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Connection connection = null;
		PreparedStatement prepareStatement = null;
		
		try {
			//1. 注册数据库驱动
			Class.forName("com.mysql.jdbc.Driver");
			//2.定义url/username/password/sql
			String url = "jdbc:mysql://192.168.189.1:3306/dbms?serverTimezone=GMT%2B8&characterEncoding=utf8&rewriteBatchedStatements=true";
			String username = "root";
			String password = "root";
			String sqlMode = "INSERT INTO `sys_users_test` VALUES ";
			String sql = "INSERT INTO `sys_users_test` VALUES (?, ?, 'c4c33035c5d8e840616c128db9f87b25', '016a0948-b581-43aa-8a5f-9bb76a80e737', 'admin@t.cn', '13624356789', 1, 2, NULL, '2020-05-08 17:21:55', NULL, NULL, ?)";
			//3.1 获取连接
			connection = DriverManager.getConnection(url, username, password);
			//3.2 设置非自动提交事务
			connection.setAutoCommit(false);
			
			StringBuffer sqlPre = new StringBuffer(sqlMode);
			
			// 开始记录时间
			long startTime = System.currentTimeMillis();
			// 发送一个空的sql骨架
			prepareStatement = connection.prepareStatement(" ");
			int i = 7000021;
			for (int x = 0; x < 200; x++) {
				for (int j = 0; j < 5000; j++) {
					sqlPre.append("(").append(i).append(",");
					sqlPre.append("'user-").append(i).append("',");
					sqlPre.append("'c4c33035c5d8e840616c128db9f87b25', '016a0948-b581-43aa-8a5f-9bb76a80e737', 'admin@t.cn', '13624356789', 1, 2, NULL, '2020-05-08 17:21:55', NULL, NULL,");
					sqlPre.append("'k").append(i).append("'),");
					i++;
				}
				// 以insert table values(...),(...),(...)形式插入
				prepareStatement.addBatch(sqlPre.substring(0, sqlPre.length()-1));
				int[] executeBatch = prepareStatement.executeBatch();
				// 清空sqlPre
				sqlPre = new StringBuffer(sqlMode);
			}
			
			// 全部执行完，再提交事务
			connection.commit();
			long endTime = System.currentTimeMillis();
			System.out.println(">>>>>>sql执行时长：[" + (endTime - startTime) + "]毫秒");
			
			//6.关闭资源
//			prepareStatement.close();
//			connection.close();
		} catch (Exception e) {
			try {
				// 回滚事务
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				//6.关闭资源
				if(null != connection) {
					// 完成批量操作后，恢复默认的自动提交，提高程序的可扩展性
					connection.setAutoCommit(true);
					connection.close();
				}
				if(null != prepareStatement)
					prepareStatement.close();	
			} catch (Exception e2) {
				
				e2.printStackTrace();
			}
		}
	}

}
