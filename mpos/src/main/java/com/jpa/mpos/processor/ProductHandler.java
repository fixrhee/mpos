package com.jpa.mpos.processor;

import java.math.BigDecimal;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jpa.mpos.data.Product;
import com.jpa.mpos.data.ServiceResponse;
import com.jpa.mpos.data.Status;
import com.jpa.mpos.data.TransactionException;

@Component
public class ProductHandler {

	@Autowired
	private ProductProcessor productProcessor;

	public ServiceResponse getAllProduct(int pageSize, int rowNum, String token, String uid) {
		try {
			HashMap<String, Object> resMap = productProcessor.getAllProduct(pageSize, rowNum, token, uid);
			return ResponseBuilder.getStatus(Status.PROCESSED, resMap);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getProductByCategory(int catID, int pageSize, int rowNum, String token, String uid) {
		try {
			HashMap<String, Object> resMap = productProcessor.getProductByCategory(catID, pageSize, rowNum, token, uid);
			return ResponseBuilder.getStatus(Status.PROCESSED, resMap);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getProductBySKU(String sku, String token, String uid) {
		try {
			Product lacq = productProcessor.getProductBySKU(sku, token, uid);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getProductByID(int id, String token, String uid) throws TransactionException {
		try {
			Product lacq = productProcessor.getProductByID(id, token, uid);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse createProduct(int categoryID, String name, String sku, String description, BigDecimal price,
			BigDecimal strikePrice, boolean publishPrice, String image, Integer stock, Integer lowStockThreshold,
			boolean publish, String token) throws TransactionException {
		try {
			productProcessor.createProduct(categoryID, name, sku, description, price, strikePrice, publishPrice, image,
					stock, lowStockThreshold, publish, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse updateProduct(int id, int categoryID, String name, String sku, String description,
			BigDecimal price, BigDecimal strikePrice, boolean publishPrice, String image, Integer stock,
			Integer lowStockThreshold, boolean publish, String token) throws TransactionException {
		try {
			productProcessor.updateProduct(id, categoryID, name, sku, description, price, strikePrice, publishPrice,
					image, stock, lowStockThreshold, publish, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse deleteProduct(int id, String token) throws TransactionException {
		try {
			productProcessor.deleteProduct(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

}
