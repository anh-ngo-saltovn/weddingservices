package com.weddingservices.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="area")
public class Area {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public int id;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "area_cd")
	public int areaCd;
	public Area() {
	}
	
	public Area(int id, String name, int areaCd){
		this.id= id;
		this.name = name;
		this.areaCd = areaCd;
	}

}
