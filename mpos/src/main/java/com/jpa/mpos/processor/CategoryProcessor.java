package com.jpa.mpos.processor;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jpa.mpos.data.Category;
import com.jpa.mpos.data.Merchant;
import com.jpa.mpos.data.Status;
import com.jpa.mpos.data.Terminal;
import com.jpa.mpos.data.TransactionException;

@Component
public class CategoryProcessor {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private Authentication authentication;

	public HashMap<String, Object> getAllCategories(int pageSize, int rowNum, String token, String uid)
			throws TransactionException {
		int id = 0;
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		if (uid.equalsIgnoreCase("NA")) {
			Merchant m = authentication.AuthenticateMerchant(token);
			if (m == null) {
				throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
			}
			if (m.isActive() == false) {
				throw new TransactionException(Status.BLOCKED);
			}
			id = m.getId();

		} else {
			Terminal m = authentication.AuthenticateTerminal(token);
			if (m == null) {
				throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
			}
			if (m.getActive() == false) {
				throw new TransactionException(Status.BLOCKED);
			}
			if (m.getUID().equalsIgnoreCase(uid) == false) {
				throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
			}
			id = m.getMerchantID();
		}
		List<Category> lacq = categoryRepository.getAllCategories(id, pageSize, rowNum);
		if (lacq.size() == 0) {
			throw new TransactionException(Status.CATEGORY_NOT_FOUND);
		}

		responseMap.put("categories", lacq);
		responseMap.put("totalRecords", categoryRepository.count());
		return responseMap;
	}

	public Category getCategoryByID(int id, String token) throws TransactionException {
		Merchant m = authentication.AuthenticateMerchant(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.isActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		Category lacq = categoryRepository.getCategoryByID(id, m.getId());
		if (lacq == null) {
			throw new TransactionException(Status.CATEGORY_NOT_FOUND);
		}
		return lacq;
	}

	public void createCategory(String name, String description, String token) throws TransactionException {
		Merchant m = authentication.AuthenticateMerchant(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.isActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		Category cat = new Category();
		cat.setName(name);
		cat.setDescription(description);
		cat.setMerchant(m);
		categoryRepository.createCategory(cat);
	}

	public void updateCategory(int id, String name, String description, String token) throws TransactionException {
		Merchant m = authentication.AuthenticateMerchant(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.isActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		Category cat = categoryRepository.getCategoryByID(id, m.getId());
		if (cat == null) {
			throw new TransactionException(Status.CATEGORY_NOT_FOUND);
		}
		cat.setName(name);
		cat.setDescription(description);
		cat.setMerchant(m);
		categoryRepository.updateCategory(cat);
	}

	public void deleteCategory(int id, String token) throws TransactionException {
		Merchant m = authentication.AuthenticateMerchant(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.isActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		Category cat = categoryRepository.getCategoryByID(id, m.getId());
		if (cat == null) {
			throw new TransactionException(Status.CATEGORY_NOT_FOUND);
		}
		categoryRepository.deleteCategory(cat.getId(), m.getId());
	}

}
