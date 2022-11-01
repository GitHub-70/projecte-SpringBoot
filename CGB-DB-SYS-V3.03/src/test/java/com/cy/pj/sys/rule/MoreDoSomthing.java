package com.cy.pj.sys.rule;


public class MoreDoSomthing {

	private LoopRule loopRule = new LoopRule(3);
	
	public double getNumber(int dotime) {
		double randomMath = loopRule.getRandomMath();
		while(true) {
			if(randomMath > 1) {
				System.out.println("第"+dotime+"次获取结果："+randomMath);
				double random = loopRule.getRandomMath();
				randomMath = random;
				dotime ++;
			} else {
				return randomMath;
			}
			
		}
	}
	
}
