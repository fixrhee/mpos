package com.jpa.mpos.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jpa.mpos.data.ServiceResponse;
import com.jpa.mpos.data.Status;
import com.jpa.mpos.data.TransactionException;

@Component
public class InvoiceHandler {

	@Autowired
	private InvoiceProcessor invoiceProcessor;

	public ServiceResponse createInvoice(String orderNo, int channelID, String name, String email, String msisdn,
			String address, String token, String uid) throws TransactionException {
		try {
			invoiceProcessor.createInvoice(orderNo, channelID, name, email, msisdn, address, token, uid);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		} catch (InterruptedException e) {
			return ResponseBuilder.getStatus(Status.UNKNOWN_ERROR, null);
		}
	}

	public ServiceResponse getInvoiceByNo(String invoiceNo, String token, String uid) throws TransactionException {
		try {
			invoiceProcessor.getInvoiceByNo(invoiceNo, token, uid);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}
}
