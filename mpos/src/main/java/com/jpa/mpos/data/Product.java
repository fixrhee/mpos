package com.jpa.mpos.data;

import java.math.BigDecimal;
import java.util.Date;

public class Product {

	private Integer id;
	private Merchant merchant;
	private Category category;
	private Inventory inventory;
	private String name;
	private String sku;
	private String description;
	private BigDecimal price;
	private BigDecimal strikePrice;
	private Boolean publishPrice;
	private String image;
	private Boolean publish;
	private Date createdDate;
	private Date modifiedDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getStrikePrice() {
		return strikePrice;
	}

	public void setStrikePrice(BigDecimal strikePrice) {
		this.strikePrice = strikePrice;
	}

	public Boolean getPublishPrice() {
		return publishPrice;
	}

	public void setPublishPrice(Boolean publishPrice) {
		this.publishPrice = publishPrice;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getPublish() {
		return publish;
	}

	public void setPublish(Boolean publish) {
		this.publish = publish;
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

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

}
