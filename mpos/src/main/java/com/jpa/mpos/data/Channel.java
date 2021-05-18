package com.jpa.mpos.data;

import java.math.BigDecimal;
import java.util.Date;

public class Channel {

	private Integer id;
	private Merchant merchant;
	private String name;
	private String description;
	private BigDecimal fixedFee;
	private double percentageFee;
	private Date createdDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getFixedFee() {
		return fixedFee;
	}

	public void setFixedFee(BigDecimal fixedFee) {
		this.fixedFee = fixedFee;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public double getPercentageFee() {
		return percentageFee;
	}

	public void setPercentageFee(double percentageFee) {
		this.percentageFee = percentageFee;
	}

}
