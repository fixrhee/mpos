package com.jpa.mpos.processor;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jpa.mpos.data.OrderInquiry;
import com.jpa.mpos.data.ServiceResponse;
import com.jpa.mpos.data.SessionToken;
import com.jpa.mpos.data.Status;
import com.jpa.mpos.data.TransactionException;

@Component
public class OrderHandler {

	@Autowired
	private OrderProcessor orderProcessor;

	public ServiceResponse getAllOrder(int terminalID, String token) {
		try {
			List<String> lacq = orderProcessor.getAllOrder(terminalID, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getOrderToken(String token) {
		try {
			SessionToken st = orderProcessor.getOrderToken(token);
			return ResponseBuilder.getStatus(Status.PROCESSED, st);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		} catch (InterruptedException e) {
			return ResponseBuilder.getStatus(Status.UNKNOWN_ERROR, null);
		}
	}

	public ServiceResponse createOrder(String orderNo, int productID, int quantity, String notes, String token) {
		try {
			orderProcessor.createOrder(orderNo, productID, quantity, notes, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse inquiryOrder(String orderNo, String token) {
		try {
			OrderInquiry oi = orderProcessor.getOrderInquiry(orderNo, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, oi);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse updateOrder(int orderID, int quantity, String notes, String token) {
		try {
			orderProcessor.updateOrder(orderID, quantity, notes, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse deleteProductFromOrder(int orderID, String token) {
		try {
			orderProcessor.deleteProductFromOrder(orderID, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse deleteOrder(String orderNo, String token) {
		try {
			orderProcessor.deleteOrder(orderNo, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

}
