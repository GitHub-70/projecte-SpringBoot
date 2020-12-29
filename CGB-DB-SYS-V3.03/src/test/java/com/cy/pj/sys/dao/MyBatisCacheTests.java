package com.cy.pj.sys.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.pojo.SysMenu;

import java.util.*;
@SpringBootTest
public class MyBatisCacheTests {
	@Autowired
	private SqlSessionFactory ssf;
	/**MyBatis一级缓存(SqlSession独享,默认开启)*/
	@Test
	void testFirstLevelCache() {
		//1.创建SqlSession对象
		SqlSession session1=ssf.openSession();
		//2.执行查询操作
		String statement="com.cy.pj.sys.dao.SysMenuDao.findObjects";
		session1.selectList(statement);
		session1.selectList(statement);//数据来自cache (一级缓存)
		//3.释放资源
		session1.close();
	}
	/**MyBatis二级缓存(SqlSession共享,默认开启,但是需要做简单配置)*/
	@Test
	void testSecondLevelCache() {
		//1.创建SqlSession对象
		SqlSession session1=ssf.openSession();
		SqlSession session2=ssf.openSession();
		//2.执行查询操作
		String statement="com.cy.pj.sys.dao.SysMenuDao.findObjects";
		long t1=System.currentTimeMillis();
		List<SysMenu> result1=session1.selectList(statement);
		session1.commit();//此时会将查询到的数据存储到二级缓存
		long t2=System.currentTimeMillis();
		System.out.println("first-query-time:"+(t2-t1));
		List<SysMenu> result2=session2.selectList(statement);//数据来自cache (二级缓存)
		long t3=System.currentTimeMillis();
		System.out.println("second-query-time:"+(t3-t2));
		System.out.println("result1==result2 ? "+(result1==result2));//false
		System.out.println("result1.equals(result2) ? "+(result1.equals(result2)));//true
		//3.释放资源
		session1.close();
		session2.close();
	}
	
}





