package com.jpa.mpos.processor;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jpa.mpos.data.Category;
import com.jpa.mpos.data.ServiceResponse;
import com.jpa.mpos.data.Status;
import com.jpa.mpos.data.TransactionException;

@Component
public class CategoryHandler {

	@Autowired
	private CategoryProcessor categoryProcessor;

	public ServiceResponse getAllCategory(int pageSize, int rowNum, String token, String uid) {
		try {
			HashMap<String, Object> lacq = categoryProcessor.getAllCategories(pageSize, rowNum, token, uid);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getCategoryByID(int id, String token) throws TransactionException {
		try {
			Category lacq = categoryProcessor.getCategoryByID(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse createCategory(String name, String description, String token) throws TransactionException {
		try {
			categoryProcessor.createCategory(name, description, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse updateCategory(int id, String name, String description, String token)
			throws TransactionException {
		try {
			categoryProcessor.updateCategory(id, name, description, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse deleteCategory(int id, String token) throws TransactionException {
		try {
			categoryProcessor.deleteCategory(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

}
