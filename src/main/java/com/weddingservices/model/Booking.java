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
import com.weddingservices.enums.StateEnum;

@Entity(name="booking")
public class Booking {
	
	@Id
	@GeneratedValue
	private int id;
	
	@Column(name="year")
	private String year;
	
	@Column(name="month")
	private String month;
	
	@Column(name="day")
	private String day;
	
	@Column(name="hour_minute")
	private String hourMinute;
	
	@Column(name="status")
	private StateEnum status;
	
	@OneToMany(mappedBy="booking", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Product> products;
	
	@OneToMany(mappedBy="booking", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Service> services;
	
	@OneToOne(mappedBy="booking", fetch=FetchType.LAZY)
	private Plan plan;
	
	@ManyToOne
	@NotNull
	private Couple couple;
	
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

	public Booking() {
	}
	
	public Booking(int id, Couple couple) {
		this.id = id;
		this.couple = couple;
	}
	
	public Booking(String year, String month, String day
			, String hourMinute, StateEnum status
			, List<Product> products, List<Service> services
			, Plan plan, Couple couple) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hourMinute = hourMinute;
		this.status = status;
		this.services = services;
		this.plan = plan;
		this.couple = couple; 
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getHourMinute() {
		return hourMinute;
	}

	public void setHourMinute(String hourMinute) {
		this.hourMinute = hourMinute;
	}

	public StateEnum getStatus() {
		return status;
	}

	public void setStatus(StateEnum status) {
		this.status = status;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Couple getCouple() {
		return couple;
	}

	public void setCouple(Couple couple) {
		this.couple = couple;
	}

	
}
