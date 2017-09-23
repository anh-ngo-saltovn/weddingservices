package com.weddingservices.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.weddingservices.enums.PointEnum;

@Entity(name="feedback")
public class FeedBack {
	
	@Id
	@GeneratedValue
	private int id;
	
	@OneToOne
	@NotNull
	private Couple couple;
	
	@OneToOne
	@NotNull
	private Service service;
	
	@Column(name="comment")
	private String comment;
	
	@Column(name="point")
	private PointEnum point;
	
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

	public FeedBack() {
	}
	
	public FeedBack(int id) {
		this.id = id;
	}
	
	public FeedBack(Couple couple, Service service, String comment, PointEnum point) {
		this.comment = comment;
		this.point = point;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Couple getCouple() {
		return couple;
	}

	public void setCouple(Couple couple) {
		this.couple = couple;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public PointEnum getPoint() {
		return point;
	}

	public void setPoint(PointEnum point) {
		this.point = point;
	}
	
	

}
