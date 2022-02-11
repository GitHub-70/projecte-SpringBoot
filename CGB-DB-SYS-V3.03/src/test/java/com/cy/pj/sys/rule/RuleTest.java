package com.cy.pj.sys.rule;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.Timeout;

/**
 * 内置Rule使用示例
 * @author Administrator
 *
 */
public class RuleTest {

	// 使用系统临时目录，可在构造方法上加入路径参数来指定临时目录
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void testTempFolderRule() throws IOException {
		// 在系统的临时目录下创建文件或者目录，当测试方法执行完毕自动删除
		tempFolder.newFile("test.txt");
		tempFolder.newFolder("test");
	}

	@Rule
	public Timeout timeout = new Timeout(1000);

	// 测试失败
	@Test
	public void test1() throws Exception {
		Thread.sleep(1001);
	}

	// 测试成功
	@Test
	public void test2() throws Exception {	
		// 总的运行时间（0.954s）不超过1秒
		Thread.sleep(650);
	}
}
