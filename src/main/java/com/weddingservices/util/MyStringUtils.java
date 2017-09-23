package com.weddingservices.util;


import java.io.UnsupportedEncodingException;

public class MyStringUtils {
	
	/**
	 * Convert Object String to UTF-8 Charset
	 * @param obj
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String  convertUTF8(Object obj) throws UnsupportedEncodingException {
		String str = "";
		if (obj instanceof String) {
			str = (String) obj;
		} else if (obj instanceof String[]) {
			 String[] strs = (String[]) obj;
			 if (strs != null && strs.length > 0) { 
				 str = strs[0];
			 }
		}
		
		byte ptext[] = str.getBytes("ISO-8859-1"); 
		return new String(ptext, "UTF-8");
	}
}
