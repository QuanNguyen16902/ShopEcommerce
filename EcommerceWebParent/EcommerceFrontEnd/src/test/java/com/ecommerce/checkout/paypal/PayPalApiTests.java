package com.ecommerce.checkout.paypal;

import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PayPalApiTests {
	private static final String BASE_URL = "https://api.sandbox.paypal.com"; 
	private static final String GET_ORDER_API = "/v2/checkout/orders/"; 
	private static final String CLIENT_ID = "AYvfuJwUvKizwiPpGUJGnoarBACyNtVdLG-4zJxfWtqoM8Sptc47lj2kdMHL7F_8uMaXDdGLJXOIXf81";
	private static final String CLIENT_SECRET = "EEDkKvAY3cgqMDRvpIv16UWwY9puIrGK1b8uq_bkxxITT0MZiy_bqFAOaFhxtk6jl5ht5kImDtY3rzZX";
	
	@Test
	public void testGetOrderDetails() {
		String orderId = "28H91799CS053783U";
		String requestURL = BASE_URL + GET_ORDER_API + orderId;
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept-language", "en-US");
		headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
	
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		
		restTemplate.exchange(requestURL, HttpMethod.GET, request, String.class);
		ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
		PayPalOrderResponse orderResponse = response.getBody();
		
		System.out.println("Order ID: " + orderResponse.getId());
		System.out.println("Order ID: " + orderResponse.validate(orderId));
	}
}
