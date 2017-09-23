package com.weddingservices.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity(name="client")
public class Client {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="address")
	private String address;
	
	@Column(name="city")
	private String city;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="area_cd")
	private int areaCd;
	
	@Column(name="company_name")
	private String companyName;
	
	@Column(name="website")
	private String website;
	
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

	@OneToMany(mappedBy="client", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<OauthClientDetails> oauthClientDetailsList = new ArrayList<OauthClientDetails>();
	
	@OneToMany(mappedBy="client", fetch= FetchType.LAZY)
	@JsonIgnore
	private List<Service> serviceList;
	
	
	public List<Service> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Service> serviceList) {
		this.serviceList = serviceList;
	}
	
	

	public List<OauthClientDetails> getOauthClientDetailsList() {
		return oauthClientDetailsList;
	}

	public void setOauthClientDetailsList(List<OauthClientDetails> oauthClientDetailsList) {
		this.oauthClientDetailsList = oauthClientDetailsList;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getAreaCd() {
		return areaCd;
	}

	public void setAreaCd(int areaCd) {
		this.areaCd = areaCd;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}


	public Client() {
	}
	
	public Client(int id) {
		this.id = id;
	}
	
	public Client(String name, String address,
			String city, String phone, int areaCd, 
			String companyName, String website, List<OauthClientDetails> oauthClientDetailsList) {
		this.name = name;
		this.address = address;
		this.city = city;
		this.phone = phone;
		this.areaCd = areaCd;
		this.companyName = companyName;
		this.website = website;
		this.oauthClientDetailsList = oauthClientDetailsList;
	}
	
	public Client(String name, String address,
			String city, String phone, int areaCd, 
			String companyName, String website) {
		this.name = name;
		this.address = address;
		this.city = city;
		this.phone = phone;
		this.areaCd = areaCd;
		this.companyName = companyName;
		this.website = website;
	}

}
