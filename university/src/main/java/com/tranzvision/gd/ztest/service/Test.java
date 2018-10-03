package com.tranzvision.gd.ztest.service;

public class Test {

	public static void main(String[] args) {
		Test t = new Test();
		//System.out.println(t.getTZ_XXX_BH("单1[0].#subform[26].surveysur8[0]"));
		int count=99;
		int size=10;
		int p = count/size;
		int m = count%size;
		System.out.println(p);
		System.out.println(m);
	}
	
	private String getTZ_XXX_BH(String str) {
		// 表单1[0].#subform[4].TZ_1124[0]
		if (str != null && str.length() > 1) {
			str = str.substring(0, str.length() - 1);
			str = str.substring(str.lastIndexOf(".") + 1, str.length());
			str = str.substring(0, str.lastIndexOf("["));
		}
		return str;
	}

}
