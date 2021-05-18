package com.jpa.mpos.processor;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jpa.mpos.data.Merchant;
import com.jpa.mpos.data.SessionToken;
import com.jpa.mpos.data.Status;
import com.jpa.mpos.data.Terminal;
import com.jpa.mpos.data.TransactionException;

@Component
public class TerminalProcessor {

	@Autowired
	private Authentication authentication;
	@Autowired
	private TerminalRepository terminalRepository;

	public Terminal getTerminalInfo(String token) throws TransactionException {
		Terminal m = authentication.AuthenticateTerminal(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.getActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}

		Terminal lacq = terminalRepository.getTerminalByID(m.getId(), m.getMerchantID());
		if (lacq == null) {
			throw new TransactionException(Status.TERMINAL_NOT_FOUND);
		}
		return lacq;
	}

	public Terminal getTerminalInfo(Integer id, String token) throws TransactionException {
		Merchant m = authentication.AuthenticateMerchant(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.isActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}

		Terminal lacq = terminalRepository.getTerminalByID(id, m.getId());
		if (lacq == null) {
			throw new TransactionException(Status.TERMINAL_NOT_FOUND);
		}
		return lacq;
	}

	public SessionToken createJWTHMAC256(String username, String secret, String uid) throws TransactionException {
		SessionToken st = new SessionToken();
		Terminal terminal = terminalRepository.validateAccess(username, secret);
		if (terminal == null) {
			throw new TransactionException(Status.ACCESS_DENIED);
		}
		terminalRepository.updateTerminalUID(terminal.getId(), uid);
		try {
			// today
			Calendar date = new GregorianCalendar();
			// reset hour, minutes, seconds and millis
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);
			// next day
			date.add(Calendar.DAY_OF_MONTH, 1);
			Date ed = date.getTime();

			String md5Hex = DigestUtils.md5Hex(secret);
			String token = JWT.create().withIssuer("Jatelindo").withSubject(username).withExpiresAt(ed)
					.sign(Algorithm.HMAC256(md5Hex));
			st.setToken(token);
			return st;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new TransactionException(Status.UNKNOWN_ERROR);
		}
	}

	public void createTerminal(Integer terminalRefID, String nns, String username, String password, String name,
			String address, String msisdn, String token) throws TransactionException {
		Merchant m = authentication.AuthenticateMerchant(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.isActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}

		Terminal term = new Terminal();
		term.setActive(true);
		term.setAddress(address);
		term.setMerchantID(m.getId());
		term.setMsisdn(msisdn);
		term.setName(name);
		term.setNnsID(nns);
		term.setPassword(password);
		term.setTerminalRefID(terminalRefID);
		term.setUsername(username);

		terminalRepository.createTerminal(term);
	}

	public void updateTerminal(Integer id, Integer terminalRefID, String nns, String username, String password,
			String name, String address, String msisdn, String token) throws TransactionException {
		Merchant m = authentication.AuthenticateMerchant(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.isActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		Terminal lacq = terminalRepository.getTerminalByID(id, m.getId());
		if (lacq == null) {
			throw new TransactionException(Status.TERMINAL_NOT_FOUND);
		}

		Terminal term = new Terminal();
		term.setActive(true);
		term.setAddress(address);
		term.setMerchantID(m.getId());
		term.setMsisdn(msisdn);
		term.setName(name);
		term.setNnsID(nns);
		term.setPassword(password);
		term.setTerminalRefID(terminalRefID);
		term.setUsername(username);

		terminalRepository.updateTerminal(id, term);
	}

	public void deleteTerminal(int id, String token) throws TransactionException {
		Merchant m = authentication.AuthenticateMerchant(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.isActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}

		Terminal lacq = terminalRepository.getTerminalByID(id, m.getId());
		if (lacq == null) {
			throw new TransactionException(Status.TERMINAL_NOT_FOUND);
		}
		terminalRepository.deleteTerminal(id, m.getId());
	}
}
