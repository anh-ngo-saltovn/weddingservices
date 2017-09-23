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
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="plan")
public class Plan {
	
	@Id
	@GeneratedValue
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="plan_price")
	private Double planPrice;
	
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

	@OneToMany(mappedBy="plan", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Product> productList;
	
	@OneToMany(mappedBy="plan", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Packages> packageList;
	
	@ManyToOne
	@NotNull
	private Couple couple;
	
	@OneToOne
	private Booking booking;
	
	public Plan() {
	}
	
	public Plan(int id, Couple couple) {
		this.id = id;
		this.couple = couple;
	}
	public Plan(String name, Double planPrice, Couple couple
			, List<Product> productList, List<Packages> packageList) {
		this.name = name;
		this.planPrice = planPrice;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(Double planPrice) {
		this.planPrice = planPrice;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public List<Packages> getPackageList() {
		return packageList;
	}

	public void setPackageList(List<Packages> packageList) {
		this.packageList = packageList;
	}

	public Couple getCouple() {
		return couple;
	}

	public void setCouple(Couple couple) {
		this.couple = couple;
	}

	
}
