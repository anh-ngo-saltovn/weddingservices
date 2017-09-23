package com.weddingservices.model;

import java.util.Date;
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

@Entity(name="service")
public class Service {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="logo")
	private String logo;
	@Column(name="description")
	private String description;
	@Column(name="address")
	private String address;
	@Column(name="area_cd")
	private String areaCd;
	@Column(name="phone")
	private String phone;
	@Column(name="facebook")
	private String facebook;
	@Column(name="website")
	private String website;
	@Column(name="type")
	private String type;
	@Column(name="lat")
	private float lat;
	@Column(name="lng")
	private float lng;
	@Column(name="status")
	private StateEnum status;
	@Column(name="create_dt")
	private Date createDt;
	@Column(name="update_dt")
	private Date updateDt;
	
	@ManyToOne
	private Booking booking;
	
	@OneToOne
	private Favorite favorite;
	
	@OneToOne(mappedBy="service", fetch=FetchType.LAZY)
	private FeedBack feedback;
	
	@ManyToOne
	@NotNull
	private Client client;
	
	@OneToMany(mappedBy="service", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Category> categoryList;
	
	@OneToMany(mappedBy="service", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<CampaignEvent> campaignEventList;
	
	@OneToMany(mappedBy="service", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Image> imageList;
	
	@OneToMany(mappedBy = "service", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Packages> packageList;
	
	public Service() {
	}
	
	public Service(int id) {
		this.id = id;
	}
	
	public StateEnum getStatus() {
		return status;
	}

	public void setStatus(StateEnum status) {
		this.status = status;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

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

	public void setLng(float lng) {
		this.lng = lng;
	}

	public String getAreaCd() {
		return areaCd;
	}

	public void setAreaCd(String areaCd) {
		this.areaCd = areaCd;
	}

	public List<CampaignEvent> getCampaignEventList() {
		return campaignEventList;
	}

	public void setCampaignEventList(List<CampaignEvent> campaignEventList) {
		this.campaignEventList = campaignEventList;
	}

	public Favorite getFavorite() {
		return favorite;
	}

	public void setFavorite(Favorite favorite) {
		this.favorite = favorite;
	}
	
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public FeedBack getFeedback() {
		return feedback;
	}

	public void setFeedback(FeedBack feedback) {
		this.feedback = feedback;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public List<Packages> getPackageList() {
		return packageList;
	}
	
	
	public void setPackageList(List<Packages> packageList) {
		this.packageList = packageList;
	}

	public List<Image> getImageList() {
		return imageList;
	}

	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}


	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
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


	public String getLogo() {
		return logo;
	}


	public void setLogo(String logo) {
		this.logo = logo;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getFacebook() {
		return facebook;
	}


	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Client getClient() {
		return client;
	}


	public void setClient(Client client) {
		this.client = client;
	}

	public Service(String name, String logo, String description, String address, String phone,
			String facebook, String type) {
		this.name = name;
		this.logo = logo;
		this.description = description;
		this.address = address;
		this.phone = phone;
		this.facebook = facebook;
		this.type = type;
	}

}
