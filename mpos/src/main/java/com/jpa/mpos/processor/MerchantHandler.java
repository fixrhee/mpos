package com.jpa.mpos.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jpa.mpos.data.Merchant;
import com.jpa.mpos.data.ServiceResponse;
import com.jpa.mpos.data.SessionToken;
import com.jpa.mpos.data.Status;
import com.jpa.mpos.data.TransactionException;

@Component
public class MerchantHandler {

	@Autowired
	private MerchantProcessor merchantProcessor;

	public ServiceResponse createJWTHMAC256(String username, String password) {
		try {
			SessionToken token = merchantProcessor.createJWTHMAC256(username, password);
			return ResponseBuilder.getStatus(Status.PROCESSED, token);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getMerchantInfo(String token) {
		try {
			Merchant lacq = merchantProcessor.getMerchantInfo(token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}
}
