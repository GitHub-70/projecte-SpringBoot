package com.cy.pj.sys.rule;

import org.junit.Rule;
import org.junit.Test;

/**
 * 自定义Rule示例
 * @author Administrator
 *
 */
public class LoopRuleTest {

	@Rule
	public LoopRule loopRule = new LoopRule(2);

	@Test
	public void testSayHello() {
		System.out.println("helloWorld");
	}
}
