package com.jpa.mpos.processor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hazelcast.core.HazelcastInstance;
import com.jpa.mpos.data.Channel;
import com.jpa.mpos.data.Invoice;
import com.jpa.mpos.data.Merchant;
import com.jpa.mpos.data.OrderProduct;
import com.jpa.mpos.data.Status;
import com.jpa.mpos.data.Terminal;
import com.jpa.mpos.data.TransactionException;
import com.noctarius.snowcast.Snowcast;
import com.noctarius.snowcast.SnowcastEpoch;
import com.noctarius.snowcast.SnowcastSequencer;
import com.noctarius.snowcast.SnowcastSystem;

@Component
public class InvoiceProcessor {

	@Autowired
	private Authentication authentication;
	@Autowired
	private ChannelRepository channelRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private MerchantRepository merchantRepository;
	@Autowired
	private HazelcastInstance hazelcastInstance;
	private SnowcastSequencer snowcastSequencer;
	private Logger logger = Logger.getLogger(this.getClass());

	@PostConstruct
	public void init() {
		Snowcast snowcast = SnowcastSystem.snowcast(hazelcastInstance);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(2014, Calendar.JANUARY, 1, 0, 0, 0);
		SnowcastEpoch epoch = SnowcastEpoch.byInstant(calendar.toInstant());
		snowcastSequencer = snowcast.createSequencer("invoiceNo", epoch);
	}

	public void createInvoice(String orderNo, int channelID, String name, String email, String msisdn, String address,
			String token, String uid) throws TransactionException, InterruptedException {
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
		Merchant merchant = merchantRepository.getMerchantByID(m.getMerchantID());

		name = name.equalsIgnoreCase("") ? null : name;
		email = email.equalsIgnoreCase("") ? null : email;
		msisdn = msisdn.equalsIgnoreCase("") ? null : msisdn;
		address = address.equalsIgnoreCase("") ? null : address;

		List<OrderProduct> lop = orderRepository.getOrderProduct(orderNo, m.getMerchantID(), m.getId());
		if (lop.size() == 0) {
			throw new TransactionException(Status.ORDER_NOT_FOUND);
		}
		List<Integer> ids = new LinkedList<Integer>();
		List<BigDecimal> subtotal = new LinkedList<BigDecimal>();

		for (int i = 0; i < lop.size(); i++) {
			ids.add(lop.get(i).getProduct().getId());
			subtotal.add(lop.get(i).getUnitPrice().multiply(new BigDecimal(lop.get(i).getQuantity())));
		}

		BigDecimal subTotalResult = subtotal.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		Channel channel = channelRepository.getChannelByID(channelID, m.getMerchantID());
		if (channel == null) {
			throw new TransactionException(Status.CHANNEL_NOT_FOUND);
		}
		BigDecimal fixedSum = channel.getFixedFee();
		BigDecimal percentageSum = subTotalResult.multiply(new BigDecimal(Double.toString(channel.getPercentageFee())))
				.divide(new BigDecimal(100));

		String invoiceNumber = String.valueOf(snowcastSequencer.next());
		Invoice inv = new Invoice();
		inv.setChannel(channel);
		if (fixedSum.compareTo(percentageSum) < 0) {
			inv.setConvenienceFee(percentageSum);
		} else if (fixedSum.compareTo(percentageSum) >= 0) {
			inv.setConvenienceFee(fixedSum);
		}
		BigDecimal total = subTotalResult.add(inv.getConvenienceFee());
		logger.info("[SUBTOTAL Products: " + subTotalResult + ", Fixed FEE: " + fixedSum + ", Percentage FEE: ("
				+ channel.getPercentageFee() + "%) " + percentageSum + ", TOTAL:" + total + "]");

		inv.setInvoiceNumber(invoiceNumber);
		inv.setMerchant(merchant);
		inv.setMsisdn(msisdn);
		inv.setName(name);
		inv.setEmail(email);
		inv.setAddress(address);
		inv.setOrderNumber(orderNo);
		inv.setSubtotal(subTotalResult);
		inv.setTerminal(m);
		inv.setTotal(total);
		invoiceRepository.createInvoice(inv);
	}

	public Invoice getInvoiceByNo(String invoiceNo, String token, String uid) throws TransactionException {
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
		Invoice inv = invoiceRepository.getInvoiceByNo(invoiceNo);
		Merchant merchant = merchantRepository.getMerchantByID(m.getMerchantID());
		Channel channel = channelRepository.getChannelByID(inv.getChannel().getId(), merchant.getId());
		inv.setMerchant(merchant);
		inv.setTerminal(m);
		inv.setChannel(channel);
		return inv;
	}

	public void updateInvoice(String invoiceNo) {
		invoiceRepository.updateInvoiceStatus(invoiceNo, "PAID");
	}

}
