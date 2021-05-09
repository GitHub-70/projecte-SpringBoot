package com.cy.pj.base;

public class StringAppendTest {

	
	
	public static void main(String[] args) {
		getStringTest1();
		
		getStringTest2();
		
		getStringTest3();
	}

	
	private static void getStringTest3() {
		String[] aa = new String[4];
		aa[0] = "00";
		aa[1] = "11";
		aa[2] = "22";
		aa[3] = "33";
		StringBuffer stringBuffer = new StringBuffer();
		StringBuffer stringBuffer2 = new StringBuffer("BB:");
		String aaa = "";
		for (int i = 0; i < aa.length; i++) {
			if (i == aa.length -1) {
				aaa = stringBuffer.append(stringBuffer2).append(aa[i]).toString();
			} else {
				aaa = stringBuffer.append(stringBuffer2).append(aa[i]).append(":").toString();
			}
			System.out.println("aaa--->"+aaa);
		}
	}
	
	private static void getStringTest2() {
		String[] aa = new String[4];
		aa[0] = "00";
		aa[1] = "11";
		aa[2] = "22";
		aa[3] = "33";
		StringBuffer stringBuffer = new StringBuffer();
		StringBuffer stringBuffer2 = new StringBuffer("BB:");
		String aaa = "";
		for (int i = 0; i < aa.length; i++) {
			if (i == aa.length -1) {
				aaa = stringBuffer.append(stringBuffer2)+aa[i];
			} else {
				aaa = stringBuffer.append(stringBuffer2)+aa[i]+":";
			}
			System.out.println("aaa--->"+aaa);
		}
	}
	
	private static void getStringTest1() {
		String[] aa = new String[4];
		aa[0] = "00";
		aa[1] = "11";
		aa[2] = "22";
		aa[3] = "33";
		StringBuffer stringBuffer2 = new StringBuffer("AA:");
		String aaa = "";
		for (int i = 0; i < aa.length; i++) {
			if (i == aa.length -1) {
				aaa += stringBuffer2+aa[i];
			} else {
				aaa += stringBuffer2+aa[i]+":";
			}
			System.out.println("aaa--->"+aaa);
		}
	}
	
}
