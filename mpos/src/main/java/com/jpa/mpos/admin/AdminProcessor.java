package com.jpa.mpos.admin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;

import com.jpa.mpos.admin.data.Login;

@Controller
public class AdminProcessor {

	public JSONObject submitLogin(Login login) throws MalformedURLException, IOException {
		String result = "";
		HttpPost post = new HttpPost("http://localhost:8900/mpos/gateway/merchant/token");

		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("username", login.getUsername()));
		urlParameters.add(new BasicNameValuePair("password", login.getPassword()));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public JSONObject memberInfo(String token) throws MalformedURLException, IOException {
		String result = "";
		HttpGet get = new HttpGet("http://localhost:8900/mpos/gateway/merchant");

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

}
