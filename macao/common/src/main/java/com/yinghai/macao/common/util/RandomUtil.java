package com.yinghai.macao.common.util;

import java.util.Random;


/**
 * 闅忔満鏁板瓧绗︿覆
 * 
 * @author TaxiGo02
 *
 */
public class RandomUtil {
	/**
	 * 获取随机码
	 * @return
	 */
	public static int getRandomInt() {
		Random random = new Random();
		return random.nextInt(999999)%900000+100000;
	}
	public static int getRandomInt(Integer weishu) {
		Random random = new Random();
		return random.nextInt(999999)%900000+100000;
	}
	public static String random(){
		int i = (int)(Math.random()*32)+1;
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < i; j++) {
			int temp = (int)(Math.random()*8)+1;
			sb.append(temp);
		}
		return sb.toString();
	}
	/**
	随机生成10位数的随机码
	*/
	public static String random10(){
		int i = (int)(Math.random()*10)+1;
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < i; j++) {
			int temp = (int)(Math.random()*8)+1;
			sb.append(temp);
		}
		return sb.toString();
	}
	public static void main(String[] args) {
		System.out.println(getRandomInt());
	}
}
