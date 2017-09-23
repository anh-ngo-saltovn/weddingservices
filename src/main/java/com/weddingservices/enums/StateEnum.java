package com.weddingservices.enums;

public enum StateEnum {
	CHO_XAC_NHAN (0, "Chờ xác nhận"),
	DA_XAC_NHAN (1, "Đã xác nhận"),
	DA_KY_HOP_DONG (2,"Đã ký hợp đồng"),
	DA_HOAN_THANH (3,"Đã hoàn thành"),
	TU_CHOI (4,"Từ chối");
	private final int value;
	private final String name;
	private StateEnum(int value, String name){
		this.value = value;
		this.name = name;
		
	}
	
	public int getValue(){
		return value;
	}
	public String getName(){
		return name;
	}
}
