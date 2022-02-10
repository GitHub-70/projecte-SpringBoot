package com.cy.pj.sys.rule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * 自定义Rule示例
 * @author Administrator
 * 
 *	自定义的Rule只需实现TestRule接口，并实现其中apply()方法即可，
 * 该方法返回一个Statement对象。
 *
 */
public class LoopRule implements TestRule {
	
    private int loopCount;

    public LoopRule(int loopCount) {
        this.loopCount = loopCount;
    }

    @Override
    public Statement apply(Statement base, Description desc) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                for (int i = 1; i <= loopCount; i++) {
                    System.out.println("Loop " + i + " started");
                    base.evaluate();
                    System.out.println("Loop " + i + " finished\n----------------");
                }
            }
        };
    }
}