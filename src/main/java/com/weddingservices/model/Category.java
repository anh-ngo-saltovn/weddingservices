package com.weddingservices.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="category")
public class Category {
	
	@Id
	@GeneratedValue
	private int id;
	
	@Column(name="name")
	private String name;
	@Column(name="code")
	private String code;
	
	@OneToMany(mappedBy="category", fetch= FetchType.LAZY)
	@JsonIgnore
	private List<Product> product_list;
	
	@ManyToOne
	@NotNull
	private Service service;
	
	@Column(name="create_dt")
	private Date createDt;
	
	@Column(name="update_dt")
	private Date updateDt;
	
	
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public Category(){
	}
	
	public Category(int id){
		this.id = id;
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public List<Product> getProductList() {
		return product_list;
	}


	public void setProductList(List<Product> productList) {
		this.product_list = productList;
	}

	public Service getService() {
		return service;
	}


	public void setService(Service service) {
		this.service = service;
	}


	
}
