package com.cy.pj.sys.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class MysqlJdbc {
	
	@Test
	void getJdbc() {
		
		try {
			//1. 注册数据库驱动
			Class.forName("com.mysql.jdbc.Driver");
			//2.定义url/username/password/sql
			String url = "jdbc:mysql://192.168.189.1:3306/dbms?serverTimezone=GMT%2B8&characterEncoding=utf8";
			String username = "root";
			String password = "root";
			String sqlMode = "INSERT INTO `sys_users_test` VALUES (1, 'admin', 'c4c33035c5d8e840616c128db9f87b25', '016a0948-b581-43aa-8a5f-9bb76a80e737', 'admin@t.cn', '13624356789', 1, 2, NULL, '2020-05-08 17:21:55', NULL, NULL, 'k1');";
			String sql = "INSERT INTO `sys_users_test` VALUES (";
			String sql2 = ", 'admin', 'c4c33035c5d8e840616c128db9f87b25', '016a0948-b581-43aa-8a5f-9bb76a80e737', 'admin@t.cn', '13624356789', 1, 2, NULL, '2020-05-08 17:21:55', NULL, NULL, 'k";
			String sql3 = "');";
			//3. 获取连接
			Connection connection = DriverManager.getConnection(url, username, password);
			PreparedStatement prepareStatement = null;
			for (int i = 21; i < 3546526; i++) {
				String sqllast = sql + String.valueOf(i) + sql2 +String.valueOf(i) + sql3;
				//4. 获取statement对象
				prepareStatement = connection.prepareStatement(sqllast);
				//5. 执行sql
				boolean execute = prepareStatement.execute();
			}
			
			//6.关闭资源
			prepareStatement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 批处理+手动提交事务：100万数据大概用时9分钟
	 * 在url中添加参数：rewriteBatchedStatements=true 100万数据大概用时一分半钟
	 * 		rewriteBatchedStatements=true 该参数添加后，insert table values (...),(...)插入数据
	 * 		验证：基础sql骨架中，以分号;结尾,报错提示中会显示
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
			String sqlMode = "INSERT INTO `sys_user_roles_test` VALUES (?, ?, ?)";
			String sql = "INSERT INTO `sys_users_test` VALUES (?, ?, 'c4c33035c5d8e840616c128db9f87b25', '016a0948-b581-43aa-8a5f-9bb76a80e737', 'admin@t.cn', '13624356789', 1, 2, NULL, '2020-05-08 17:21:55', NULL, NULL, ?)";
			//3.1 获取连接
			connection = DriverManager.getConnection(url, username, password);
			//3.2 设置非自动提交事务
			connection.setAutoCommit(false);
			//4. 获取statement对象 装载sql骨架
//			prepareStatement = connection.prepareStatement(sqlMode);
			prepareStatement = connection.prepareStatement(sql);
			// java.sql.Date
			java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
			
			int y = 5000021;
			// 开始记录时间
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < 1000000; i++) {
				// 设置动态参数
				prepareStatement.setInt(1, y);//给sql中的第一个?赋值
				prepareStatement.setString(2, "user-" + String.valueOf(y));
				prepareStatement.setString(3, "k" + String.valueOf(y));
				y++;
				prepareStatement.addBatch();
				
//				prepareStatement.setInt(1, i+1);
//				prepareStatement.setInt(2, i+1);
//				prepareStatement.setInt(3, i+1);
//				prepareStatement.addBatch();
				
				//错误 SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near '?
//				prepareStatement.addBatch(sql);
				
				//5.1 当积攒到一定数量，统一执行一次，并清空积攒的sql, TODO mysql批处理理论值 62500条/秒？
				if((i+1) % 62500 == 0) {
					int[] executeBatch = prepareStatement.executeBatch();
					for (int j = 0; j < executeBatch.length; j++) {
						if(j == 10000) {
							System.out.println("executeBatch:"+executeBatch[j]);
						}
					}
					prepareStatement.clearBatch();
					// 一批提交一次--性能较慢
//					connection.commit();
				}
			}
			
			//5.2 若总数量不是批处理数值的整数倍，还行额外的执行一次
			if(1000000 % 62500 != 0) {
				prepareStatement.executeBatch();
				prepareStatement.clearBatch();
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
