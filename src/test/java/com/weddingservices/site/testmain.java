package com.weddingservices.site;

import org.springframework.beans.factory.annotation.Autowired;

import com.weddingservices.util.UserAuthentication;

public class testmain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserAuthentication u = new UserAuthentication();
				System.out.println(u.encodePassword("abc123"));
				
				
	}
	
	public class Base {
		String s = "123";
		void doit() {
			System.out.println("doit " + s );
		}
	}

	public class New extends Base{
		String s = "456";
		void doit() {
			System.out.println("doit " + s);
		}
	}

}
