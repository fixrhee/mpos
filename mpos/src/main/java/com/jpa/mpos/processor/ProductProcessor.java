package com.jpa.mpos.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jpa.mpos.data.Category;
import com.jpa.mpos.data.Inventory;
import com.jpa.mpos.data.Merchant;
import com.jpa.mpos.data.Product;
import com.jpa.mpos.data.Status;
import com.jpa.mpos.data.Terminal;
import com.jpa.mpos.data.TransactionException;

@Component
public class ProductProcessor {

	@Autowired
	private Authentication authentication;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private InventoryRepository inventoryRepository;
	@Autowired
	private MerchantRepository merchantRepository;

	public HashMap<String, Object> getAllProduct(int pageSize, int rowNum, String token, String uid)
			throws TransactionException {
		int id = 0;
		Merchant merchant = null;
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		if (uid.equalsIgnoreCase("NA")) {
			Merchant m = authentication.AuthenticateMerchant(token);
			if (m == null) {
				throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
			}
			if (m.isActive() == false) {
				throw new TransactionException(Status.BLOCKED);
			}
			merchant = m;
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
			merchant = merchantRepository.getMerchantByID(m.getMerchantID());
			id = m.getMerchantID();
		}

		List<Product> lacq = productRepository.getAllProducts(id, pageSize, rowNum);
		if (lacq.size() == 0) {
			throw new TransactionException(Status.PRODUCT_NOT_FOUND);
		}

		List<Integer> cids = new LinkedList<Integer>();
		List<Integer> pids = new LinkedList<Integer>();
		for (int i = 0; i < lacq.size(); i++) {
			cids.add(lacq.get(i).getCategory().getId());
			pids.add(lacq.get(i).getId());
		}

		Map<Integer, Inventory> im = inventoryRepository.getInventoryInMap(pids);
		Map<Integer, Category> pm = categoryRepository.getCategoryInMap(cids);
		List<Product> lvc = new ArrayList<Product>(lacq);

		for (int i = 0; i < lacq.size(); i++) {
			lvc.get(i).setCategory(pm.get(lvc.get(i).getCategory().getId()));
			lvc.get(i).setInventory(im.get(lvc.get(i).getId()));
			lvc.get(i).setMerchant(merchant);
		}

		responseMap.put("products", lvc);
		responseMap.put("totalRecords", productRepository.count());
		return responseMap;
	}

	public HashMap<String, Object> getProductByCategory(int cid, int pageSize, int rowNum, String token, String uid)
			throws TransactionException {
		int id = 0;
		Merchant merchant = null;
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		if (uid.equalsIgnoreCase("NA")) {
			Merchant m = authentication.AuthenticateMerchant(token);
			if (m == null) {
				throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
			}
			if (m.isActive() == false) {
				throw new TransactionException(Status.BLOCKED);
			}
			merchant = m;
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
			merchant = merchantRepository.getMerchantByID(m.getMerchantID());
		}

		List<Product> lacq = productRepository.getProductByCategory(id, cid, pageSize, rowNum);
		if (lacq.size() == 0) {
			throw new TransactionException(Status.PRODUCT_NOT_FOUND);
		}
		List<Integer> cids = new LinkedList<Integer>();
		List<Integer> pids = new LinkedList<Integer>();
		for (int i = 0; i < lacq.size(); i++) {
			cids.add(lacq.get(i).getCategory().getId());
			pids.add(lacq.get(i).getId());
		}

		Map<Integer, Inventory> im = inventoryRepository.getInventoryInMap(pids);
		Map<Integer, Category> pm = categoryRepository.getCategoryInMap(cids);
		List<Product> lvc = new ArrayList<Product>(lacq);

		for (int i = 0; i < lacq.size(); i++) {
			lvc.get(i).setCategory(pm.get(lvc.get(i).getCategory().getId()));
			lvc.get(i).setInventory(im.get(lvc.get(i).getId()));
			lvc.get(i).setMerchant(merchant);
		}
		responseMap.put("products", lvc);
		responseMap.put("totalRecords", productRepository.count());
		return responseMap;
	}

	public Product getProductByID(int pid, String token, String uid) throws TransactionException {
		int id = 0;
		Merchant merchant = null;
		if (uid.equalsIgnoreCase("NA")) {
			Merchant m = authentication.AuthenticateMerchant(token);
			if (m == null) {
				throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
			}
			if (m.isActive() == false) {
				throw new TransactionException(Status.BLOCKED);
			}
			merchant = m;
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
			merchant = merchantRepository.getMerchantByID(m.getMerchantID());
		}
		Product lacq = productRepository.getProductByID(pid, id);
		if (lacq == null) {
			throw new TransactionException(Status.PRODUCT_NOT_FOUND);
		}
		Inventory inv = inventoryRepository.getStockByProductID(lacq.getId(), id);
		lacq.setMerchant(merchant);
		lacq.setInventory(inv);
		return lacq;
	}

	public Product getProductBySKU(String sku, String token, String uid) throws TransactionException {
		int id = 0;
		Merchant merchant = null;
		if (uid.equalsIgnoreCase("NA")) {
			Merchant m = authentication.AuthenticateMerchant(token);
			if (m == null) {
				throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
			}
			if (m.isActive() == false) {
				throw new TransactionException(Status.BLOCKED);
			}
			merchant = m;
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
			merchant = merchantRepository.getMerchantByID(m.getMerchantID());
		}
		Product lacq = productRepository.getProductBySKU(sku, id);
		if (lacq == null) {
			throw new TransactionException(Status.PRODUCT_NOT_FOUND);
		}
		Inventory inv = inventoryRepository.getStockByProductID(lacq.getId(), id);
		lacq.setMerchant(merchant);
		lacq.setInventory(inv);
		return lacq;
	}

	public void createProduct(int categoryID, String name, String sku, String description, BigDecimal price,
			BigDecimal strikePrice, boolean publishPrice, String image, Integer stock, Integer lowStockThreshold,
			boolean publish, String token) throws TransactionException {
		Merchant m = authentication.AuthenticateMerchant(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.isActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		Product prod = new Product();
		Category cat = new Category();
		cat.setId(categoryID);
		prod.setCategory(cat);
		prod.setDescription(description);
		prod.setImage(image);
		prod.setMerchant(m);
		prod.setName(name);
		prod.setPrice(price);
		prod.setPublish(publish);
		prod.setPublishPrice(publishPrice);
		prod.setSku(sku);
		prod.setStrikePrice(strikePrice);
		long pid = productRepository.createProduct(prod);
		inventoryRepository.createStock(pid, m.getId(), stock, lowStockThreshold);
	}

	public void updateProduct(int id, int categoryID, String name, String sku, String description, BigDecimal price,
			BigDecimal strikePrice, boolean publishPrice, String image, Integer stock, Integer lowStockThreshold,
			boolean publish, String token) throws TransactionException {
		Merchant m = authentication.AuthenticateMerchant(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.isActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		Product p = productRepository.getProductByID(id, m.getId());
		if (p == null) {
			throw new TransactionException(Status.PRODUCT_NOT_FOUND);
		}

		Product prod = new Product();
		Category cat = new Category();
		cat.setId(categoryID);

		prod.setId(p.getId());
		prod.setCategory(cat);
		prod.setDescription(description);
		prod.setImage(image);
		prod.setMerchant(m);
		prod.setName(name);
		prod.setPrice(price);
		prod.setPublish(publish);
		prod.setPublishPrice(publishPrice);
		prod.setSku(sku);
		prod.setStrikePrice(strikePrice);
		productRepository.updateProduct(id, prod);
		inventoryRepository.updateStock(m.getId(), id, stock, lowStockThreshold);
	}

	public void deleteProduct(int id, String token) throws TransactionException {
		Merchant m = authentication.AuthenticateMerchant(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.isActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		Product p = productRepository.getProductByID(id, m.getId());
		if (p == null) {
			throw new TransactionException(Status.PRODUCT_NOT_FOUND);
		}
		productRepository.deleteProduct(m.getId(), id);
		inventoryRepository.deleteStock(m.getId(), p.getId());
	}

}
