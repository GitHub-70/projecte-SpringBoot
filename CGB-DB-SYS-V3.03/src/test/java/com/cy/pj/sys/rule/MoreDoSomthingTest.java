package com.cy.pj.sys.rule;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
public class MoreDoSomthingTest {
	
	@InjectMocks
	private MoreDoSomthing moreDoSomthing;
	
	@Mock
	private LoopRule loopRule;
	
	@Test
	public void should_return_dif_result_when_do_sameMethod() {
		double expected = 0.5;
		Mockito.when(loopRule.getRandomMath())
		.thenReturn(2.0)//第一次返回 2.0
		.thenReturn(0.5);//第二次返回 0.5
		double actual = moreDoSomthing.getNumber(1);
		
		Mockito.verify(loopRule, Mockito.times(2))
		.getRandomMath();
		Assert.assertEquals(expected, actual);
		
	}

}
