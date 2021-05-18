package com.jpa.mpos.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jpa.mpos.data.ServiceResponse;
import com.jpa.mpos.data.SessionToken;
import com.jpa.mpos.data.Status;
import com.jpa.mpos.data.Terminal;
import com.jpa.mpos.data.TransactionException;

@Component
public class TerminalHandler {

	@Autowired
	private TerminalProcessor terminalProcessor;

	public ServiceResponse createJWTHMAC256(String username, String password, String uid) {
		try {
			SessionToken token = terminalProcessor.createJWTHMAC256(username, password, uid);
			return ResponseBuilder.getStatus(Status.PROCESSED, token);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getTerminalInfo(Integer id, String token) {
		try {
			Terminal lacq = terminalProcessor.getTerminalInfo(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getTerminalInfo(String token) {
		try {
			Terminal lacq = terminalProcessor.getTerminalInfo(token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse createTerminal(Integer terminalRefID, String nns, String username, String password,
			String name, String address, String msisdn, String token) throws TransactionException {
		try {
			terminalProcessor.createTerminal(terminalRefID, nns, username, password, name, address, msisdn, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse updateTerminal(Integer id, Integer terminalRefID, String nns, String username,
			String password, String name, String address, String msisdn, String token) throws TransactionException {
		try {
			terminalProcessor.updateTerminal(id, terminalRefID, nns, username, password, name, address, msisdn, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse deleteTerminal(int id, String token) throws TransactionException {
		try {
			terminalProcessor.deleteTerminal(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

}
