package com.weddingservices.enums;

public enum PointEnum {
	QUA_TE ("1"),
	KHONG_TOT ("2"),
	DAT_CHUAN ("3"),
	TOT ("4"),
	TUYET_VOI ("5");
	private final String value;
	private PointEnum(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}
