package com.jpa.mpos.data;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Merchant {

	private Integer id;
	private String parentUsername;
	private String username;
	private String name;
	private String email;
	private String password;
	private Boolean active;
	private String msisdn;
	private String UID;
	private Date createdDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public String getParentUsername() {
		return parentUsername;
	}

	public void setParentUsername(String parentUsername) {
		this.parentUsername = parentUsername;
	}

}
