package com.weddingservices.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity(name="couple")
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"email"})})
public class Couple {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="name_nam")
	private String nameNam;
	@Column(name="name_nu")
	private String nameNu;
	@Column(name="phone_nam")
	private String phoneNam;
	@Column(name="phone_nu")
	private String phoneNu;
	@Column(name="address")
	private String address;
	@Column(name="email")
	private String email;
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

	@OneToOne(mappedBy="couple", fetch= FetchType.LAZY)
	private OauthClientDetails oauthClientDetail;
	
	@OneToOne(mappedBy="couple", fetch= FetchType.LAZY)
	private FeedBack feedback;
	
	@OneToMany(mappedBy="couple", fetch=FetchType.LAZY)
	private List<Favorite> favorite;
	
	@OneToMany(mappedBy="couple", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Plan> planList;
	
	@OneToMany(mappedBy="couple", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Booking> bookingList;
	
	public Couple() {
	}
	
	public Couple(int id) {
		this.id = id;
	}
	
	public Couple(String nameNam, String nameNu,
				String phoneNam, String phoneNu, String address, String email, OauthClientDetails oauthClientDetail) {
		this.nameNam = nameNam;
		this.nameNu = nameNu;
		this.address = address;
		this.phoneNam = phoneNam;
		this.phoneNu = phoneNu;
		this.email = email;
		this.oauthClientDetail = oauthClientDetail;
	}
	
	
	
	public FeedBack getFeedback() {
		return feedback;
	}

	public void setFeedback(FeedBack feedback) {
		this.feedback = feedback;
	}

	public List<Booking> getBookingList() {
		return bookingList;
	}

	public void setBookingList(List<Booking> bookingList) {
		this.bookingList = bookingList;
	}

	public List<Plan> getPlanList() {
		return planList;
	}

	public void setPlanList(List<Plan> planList) {
		this.planList = planList;
	}

	

	public List<Favorite> getFavorite() {
		return favorite;
	}

	public void setFavorite(List<Favorite> favorite) {
		this.favorite = favorite;
	}

	public OauthClientDetails getOauthClientDetail() {
		return oauthClientDetail;
	}

	public void setOauthClientDetail(OauthClientDetails oauthClientDetail) {
		this.oauthClientDetail = oauthClientDetail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNameNam() {
		return nameNam;
	}

	public void setNameNam(String nameNam) {
		this.nameNam = nameNam;
	}

	public String getNameNu() {
		return nameNu;
	}

	public void setNameNu(String nameNu) {
		this.nameNu = nameNu;
	}

	public String getPhoneNam() {
		return phoneNam;
	}

	public void setPhoneNam(String phoneNam) {
		this.phoneNam = phoneNam;
	}

	public String getPhoneNu() {
		return phoneNu;
	}

	public void setPhoneNu(String phoneNu) {
		this.phoneNu = phoneNu;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
