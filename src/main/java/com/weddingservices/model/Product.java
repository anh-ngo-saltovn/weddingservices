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

@Entity(name="product")
public class Product {
	
	@Id
	@GeneratedValue
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;
	
	@Column(name="quality")
	private int quality;
	@Column(name="price")
	private Double price;
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
	private Booking booking;
	
	@ManyToOne
	@NotNull
	private Category category;
	
	@OneToMany(mappedBy="product", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Image> imageList;
	
	@ManyToOne
	private Plan plan;
	
	@ManyToOne
	private Packages packages;
	
	public Plan getPlan() {
		return plan;
	}
	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	public Packages getPackages() {
		return packages;
	}
	public void setPackage1(Packages packages) {
		this.packages = packages;
	}
	public List<Image> getImageList() {
		return imageList;
	}
	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public Booking getBooking() {
		return booking;
	}
	public void setBooking(Booking booking) {
		this.booking = booking;
	}
	public void setPackages(Packages packages) {
		this.packages = packages;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Product(){
	}
	
	public Product(int id){
		this.id = id;
	}
	public Product(String name, String description, int quality, Double price,String type) {
		super();
		this.name = name;
		this.description = description;
		this.quality = quality;
		this.price = price;
	}

}
