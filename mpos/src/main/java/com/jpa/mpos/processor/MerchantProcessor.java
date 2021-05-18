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
import com.jpa.mpos.data.TransactionException;

@Component
public class MerchantProcessor {

	@Autowired
	private Authentication authentication;
	@Autowired
	private MerchantRepository merchantRepository;

	public Merchant getMerchantInfo(String token) throws TransactionException {
		Merchant m = (Merchant) authentication.AuthenticateMerchant(token);
		if (m == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		if (m.isActive() == false) {
			throw new TransactionException(Status.BLOCKED);
		}
		Merchant lacq = merchantRepository.getMerchantByID(m.getId());
		if (lacq == null) {
			throw new TransactionException(Status.MEMBER_NOT_FOUND);
		}
		return lacq;
	}

	public SessionToken createJWTHMAC256(String username, String secret) throws TransactionException {
		SessionToken st = new SessionToken();
		Merchant member = merchantRepository.validateAccess(username, secret);
		if (member == null) {
			throw new TransactionException(Status.ACCESS_DENIED);
		}
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
}
