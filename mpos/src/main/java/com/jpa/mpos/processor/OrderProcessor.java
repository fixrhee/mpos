package com.jpa.mpos.processor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.jpa.mpos.data.Merchant;
import com.jpa.mpos.data.Order;
import com.jpa.mpos.data.OrderInquiry;
import com.jpa.mpos.data.OrderProduct;
import com.jpa.mpos.data.Product;
import com.jpa.mpos.data.SessionToken;
import com.jpa.mpos.data.Status;
import com.jpa.mpos.data.Terminal;
import com.jpa.mpos.data.TransactionException;
import com.noctarius.snowcast.Snowcast;
import com.noctarius.snowcast.SnowcastEpoch;
import com.noctarius.snowcast.SnowcastSequencer;
import com.noctarius.snowcast.SnowcastSystem;

@Component
public class OrderProcessor {

	@Autowired
	private Authentication authentication;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private HazelcastInstance hazelcastInstance;
	private SnowcastSequencer snowcastSequencer;

	@PostConstruct
	public void init() {
		Snowcast snowcast = SnowcastSystem.snowcast(hazelcastInstance);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(2014, Calendar.JANUARY, 1, 0, 0, 0);
		SnowcastEpoch epoch = SnowcastEpoch.byInstant(calendar.toInstant());
		snowcastSequencer = snowcast.createSequencer("orderNo", epoch);
	}

	public List<String> getAllOrder(int tid, String token) throws TransactionException {
		Terminal ta = authentication.AuthenticateTerminal(token);
		if (ta == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (ta.getActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		List<String> orderno = orderRepository.getAllOrderNo(ta.getMerchantID(), tid);
		return orderno;
	}

	public SessionToken getOrderToken(String token) throws TransactionException, InterruptedException {
		Terminal ta = authentication.AuthenticateTerminal(token);
		if (ta == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (ta.getActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		String uid = String.valueOf(snowcastSequencer.next());
		IMap<String, Terminal> orderMap = hazelcastInstance.getMap("Order");
		orderMap.put(uid, ta);

		SessionToken st = new SessionToken();
		st.setToken(uid);
		return st;
	}

	public void createOrder(String orderNo, Integer productID, Integer quantity, String notes, String token)
			throws TransactionException {
		Terminal ta = authentication.AuthenticateTerminal(token);
		if (ta == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (ta.getActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		IMap<String, Terminal> orderMap = hazelcastInstance.getMap("Order");
		Terminal tmap = orderMap.get(orderNo);
		if (tmap == null) {
			throw new TransactionException(Status.INVALID_PARAMETER);
		}
		if (tmap.getId().compareTo(ta.getId()) != 0 || tmap.getMerchantID().compareTo(ta.getMerchantID()) != 0) {
			throw new TransactionException(Status.TERMINAL_NOT_FOUND);
		}
		Product product = productRepository.getProductByID(productID, ta.getMerchantID());
		if (product == null) {
			throw new TransactionException(Status.PRODUCT_NOT_FOUND);
		}

		// IF Product + Terminal + MID + OrderToken exist --> DO UPDATE ORDER (SUM
		// quantity and update notes)
		Order order = new Order();
		Integer oid = orderRepository.getOrderProduct(ta.getMerchantID(), ta.getId(), productID, orderNo);
		if (oid != null) {
			orderRepository.updateOrderQuantity(oid, quantity + 1);
		} else {
			Merchant merchant = new Merchant();
			merchant.setId(ta.getMerchantID());
			order.setMerchant(merchant);
			order.setNotes(notes);
			order.setOrderNo(orderNo);
			order.setProduct(product);
			order.setTerminal(ta);
			order.setUnitPrice(product.getPrice());

			if (quantity == null) {
				order.setQuantity(1);
			} else {
				order.setQuantity(quantity);
			}
			orderRepository.createOrder(order);
		}
	}

	public OrderInquiry getOrderInquiry(String orderNo, String token) throws TransactionException {
		Terminal ta = authentication.AuthenticateTerminal(token);
		if (ta == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (ta.getActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}

		List<OrderProduct> lop = orderRepository.getOrderProduct(orderNo, ta.getMerchantID(), ta.getId());
		List<Integer> ids = new LinkedList<Integer>();
		List<BigDecimal> subtotal = new LinkedList<BigDecimal>();

		for (int i = 0; i < lop.size(); i++) {
			ids.add(lop.get(i).getProduct().getId());
			subtotal.add(lop.get(i).getUnitPrice().multiply(new BigDecimal(lop.get(i).getQuantity())));
		}

		Map<Integer, Product> mp = productRepository.getProductInMap(ids);
		for (int i = 0; i < lop.size(); i++) {
			lop.get(i).setProduct(mp.get(lop.get(i).getProduct().getId()));
		}

		BigDecimal subTotalResult = subtotal.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		OrderInquiry oi = new OrderInquiry();
		oi.setProducts(lop);
		oi.setOrderNo(orderNo);
		oi.setSubTotal(subTotalResult);
		oi.setDiscount(BigDecimal.ZERO);
		oi.setTax(BigDecimal.ZERO);
		oi.setTotal(subTotalResult);
		return oi;
	}

	public void updateOrder(int orderID, Integer quantity, String notes, String token) throws TransactionException {
		Terminal ta = authentication.AuthenticateTerminal(token);
		if (ta == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (ta.getActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		if (orderRepository.getOrderProduct(orderID) == null) {
			throw new TransactionException(Status.ORDER_NOT_FOUND);
		}

		orderRepository.updateOrder(orderID, quantity, notes);
	}

	public void deleteProductFromOrder(int orderID, String token) throws TransactionException {
		Terminal ta = authentication.AuthenticateTerminal(token);
		if (ta == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (ta.getActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		if (orderRepository.getOrderProduct(orderID) == null) {
			throw new TransactionException(Status.ORDER_NOT_FOUND);
		}
		orderRepository.deleteProductFromOrder(orderID);
	}

	public void deleteOrder(String orderNo, String token) throws TransactionException {
		Terminal ta = authentication.AuthenticateTerminal(token);
		if (ta == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (ta.getActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		orderRepository.deleteOrder(orderNo, ta.getMerchantID(), ta.getId());
	}
}
