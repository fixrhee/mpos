package com.jpa.mpos.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jpa.mpos.data.Merchant;
import com.jpa.mpos.data.Terminal;

@Component
public class Authentication {

	@Autowired
	private MerchantRepository merchantRepository;
	@Autowired
	private TerminalRepository terminalRepository;

	public DecodedJWT verifyJWTHMAC256(String token, String secret) throws Exception {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).withIssuer("Jatelindo").build();
		DecodedJWT jwt = verifier.verify(token);
		return jwt;
	}

	public String decodeJWTHMAC256(String token) throws Exception {
		DecodedJWT jwt = JWT.decode(token);
		return jwt.getSubject();
	}

	public Terminal AuthenticateTerminal(String token) {
		try {
			String username = decodeJWTHMAC256(token);
			Terminal terminal = terminalRepository.getTerminalByUsername(username);
			verifyJWTHMAC256(token, terminal.getPassword());
			return terminal;
		} catch (Exception e) {
			return null;
		}
	}

	public Merchant AuthenticateMerchant(String token) {
		try {
			String username = decodeJWTHMAC256(token);
			Merchant member = merchantRepository.getMerchantByUsername(username);
			verifyJWTHMAC256(token, member.getPassword());
			return member;
		} catch (Exception e) {
			return null;
		}
	}
}
