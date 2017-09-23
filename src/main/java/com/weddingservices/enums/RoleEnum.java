package com.weddingservices.enums;

public enum RoleEnum {
	ROLE_ADMIN (3,"Admin"),
	ROLE_CLIENT (1,"Client"),
	ROLE_COUPLE (2,"Couple");
	private final int value;
	private final String name;
	private RoleEnum(int value, String name){
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
