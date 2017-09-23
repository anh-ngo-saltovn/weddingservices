package com.weddingservices.enums;

public enum ServiceTypeEnum {
	QUA_CUOI_AN_HOI (1,"Quả cưới, ăn hỏi"),
	CHUP_HINH (2,"Chụp hình"),
	THIEP_CUOI (3,"Thiệp cưới"),
	BANH_CUOI (4,"Bánh cưới"),
	HOA (5,"Hoa cưới"),
	AO_CUOI (6,"Áo cưới"),
	TRANG_TRI (7,"Trang trí"),
	NHA_HANG (8,"Nhà hàng"),
	HONEY_MOON (9,"Resort");
	private final int value;
	private final String name;
	private ServiceTypeEnum(int value, String name){
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
