package com.jpa.mpos.data;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;


public class Terminal implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3102711913084664140L;
	private Integer id;
	private Integer terminalRefID;
	private Integer merchantID;
	private String nnsID;
	private String UID;
	private String username;
	private String password;
	private String name;
	private String address;
	private String msisdn;
	private Boolean active;
	private Date createdDate;
	private Date modifiedDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTerminalRefID() {
		return terminalRefID;
	}

	public void setTerminalRefID(Integer terminalRefID) {
		this.terminalRefID = terminalRefID;
	}

	public Integer getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(Integer merchantID) {
		this.merchantID = merchantID;
	}

	public String getNnsID() {
		return nnsID;
	}

	public void setNnsID(String nnsID) {
		this.nnsID = nnsID;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Boolean getActive() {
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
