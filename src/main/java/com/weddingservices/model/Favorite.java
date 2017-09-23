package com.weddingservices.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity(name="favorite")
public class Favorite {
	
	@Id
	@GeneratedValue
	private int id;
	
	@OneToOne(mappedBy="favorite", fetch=FetchType.LAZY)
	private Service service;
	
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
	public Favorite() {
	}
	public Favorite(int id) {
		this.id = id;
	}
	
	public Favorite(Service service, Couple couple) {
		this.service = service;
		this.couple = couple;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	public Couple getCouple() {
		return couple;
	}
	public void setCouple(Couple couple) {
		this.couple = couple;
	}

}
