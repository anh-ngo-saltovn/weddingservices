package com.weddingservices.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;


@Entity(name="oauth_client_details")
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"client_id"})})
public class OauthClientDetails {
	
	@Id
	@NotNull
	@Column(name="client_id")
	private String id;
	
	@JsonIgnore
	private String resource_ids;
	
	@NotNull
	@JsonIgnore
	private String client_secret;
	
	@JsonIgnore
	private String scope;
	
	@JsonIgnore
	private String authorized_grant_types;
	@JsonIgnore
	private String web_server_redirect_uri;
	@JsonIgnore
	private String authorities;
	
	@JsonIgnore
	private int access_token_validity;
	@JsonIgnore
	private int refresh_token_validity;
	@JsonIgnore
	private String additional_information;
	@JsonIgnore
	private String autoapprove;
	
	private Date createDt;
	private Date updateDt;
	
	
	
//	
//	@NotNull
//	private String password;
	

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
	@JoinColumn(name="client_ids", referencedColumnName="id")
	private Client client;
	
	
	@OneToOne
	private Couple couple;
	
	public Couple getCouple() {
		return couple;
	}

	public void setCouple(Couple couple) {
		this.couple = couple;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	


	public int getAccess_token_validity() {
		return access_token_validity;
	}

	public void setAccess_token_validity(int access_token_validity) {
		this.access_token_validity = access_token_validity;
	}
	
	public int getRefresh_token_validity() {
		return refresh_token_validity;
	}

	public void setRefresh_token_validity(int refresh_token_validity) {
		this.refresh_token_validity = refresh_token_validity;
	}

	public String getAutoapprove() {
		return autoapprove;
	}

	public void setAutoapprove(String autoapprove) {
		this.autoapprove = autoapprove;
	}

	public String getResource_ids() {
		return resource_ids;
	}

	public void setResource_ids(String resource_ids) {
		this.resource_ids = resource_ids;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAuthorized_grant_types() {
		return authorized_grant_types;
	}

	public void setAuthorized_grant_types(String authorized_grant_types) {
		this.authorized_grant_types = authorized_grant_types;
	}

	public String getWeb_server_redirect_uri() {
		return web_server_redirect_uri;
	}

	public void setWeb_server_redirect_uri(String web_server_redirect_uri) {
		this.web_server_redirect_uri = web_server_redirect_uri;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public String getAdditional_information() {
		return additional_information;
	}

	public void setAdditional_information(String additional_information) {
		this.additional_information = additional_information;
	}

	public OauthClientDetails(String resource_ids
			, String client_secret, String scope, String authorized_grant_types
			, String web_server_redirect_uri, String authorities, int access_token_validty
			, int refesh_token_validty, String additional_information, String autoapprove_information) {
		super();
		this.resource_ids = resource_ids;
		this.client_secret = client_secret;
		this.scope = scope;
		this.authorized_grant_types = authorized_grant_types;
		this.web_server_redirect_uri = web_server_redirect_uri;
		this.authorities = authorities;
		this.access_token_validity = access_token_validty;
		this.refresh_token_validity = refesh_token_validty;
		this.additional_information = additional_information;
		this.autoapprove = autoapprove_information;
	}
	
	
	public OauthClientDetails(){
		
	}
	
	public OauthClientDetails(String id){
		this.id = id;
		
	}
	

}
