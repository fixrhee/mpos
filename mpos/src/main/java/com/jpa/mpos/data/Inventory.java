package com.jpa.mpos.data;

import java.util.Date;

public class Inventory {

	private Integer id;
	private Product product;
	private Merchant merchant;
	private Integer stock;
	private Integer lowStockThreshold;
	private Date lastUpdate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getLowStockThreshold() {
		return lowStockThreshold;
	}

	public void setLowStockThreshold(Integer lowStockThreshold) {
		this.lowStockThreshold = lowStockThreshold;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
