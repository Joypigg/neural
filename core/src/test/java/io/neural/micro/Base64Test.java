package io.neural.micro;

public class Base64Test {

	public static void main(String[] args) {
		System.out.println(Base64.getUrlEncoder().encodeToString("测试报文".getBytes()));
	}
	
}
