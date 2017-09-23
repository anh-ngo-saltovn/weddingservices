package com.weddingservices.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity(name="campaign_event")
public class CampaignEvent {

	@Id
	@GeneratedValue
	private int id;
	
	@Column(name="description")
	private String description;
	
	@Column(name="code")
	@NotNull
	private String code;
	
	@Column(name="start_date")
	@NotNull
	private Date startDate;
	
	@Column(name="end_date")
	@NotNull
	private Date endDate;
	
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

	public CampaignEvent() {
	}
	
	public CampaignEvent(String code, String description, 
			Date startDate, Date endDate,Service service) {
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.service = service;
		this.code = code;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}
	
	

}
