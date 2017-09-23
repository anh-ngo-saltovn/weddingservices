package com.weddingservices.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name="image")
public class Image {

	@Id
	@GeneratedValue
	private int id;
	
	@Column(name="image_path")
	private String imagePath;
	
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

	@ManyToOne
	private Service service;
	
	@ManyToOne
	private Product product;
	
	public Image() {
	}
	
	public Image(int id) {
		this.id = id;
	}
	public Image(String path, Service service, Product product) {
		this.imagePath = path;
		this.service = service;
		this.product = product;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String path) {
		this.imagePath = path;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	

}
