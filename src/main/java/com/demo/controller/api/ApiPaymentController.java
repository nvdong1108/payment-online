package com.demo.controller.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.entity.Deposits;
import com.demo.repository.DepositsRepository;
import com.demo.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiPaymentController {

	private String gatewayUrl = "https://gw.paywb.co/directapi";
	private String path_webhook = "http://localhost:8090/api/payment/webhook";
	private String path_return = "http://localhost:8090/payment/return";
	private String path_checkout = "http://localhost:8090/checkout";

	@Autowired
	private DepositsRepository depositsRepository;


	@Autowired
	private TransactionService transactionService;

	@Value("${gw.paywb.co.publicKey}")
	String publicKey;

	@Value("${gw.paywb.co.terNo}")
	String terNo;

	@PostMapping("/payment/checkout")
	public ResponseEntity<?> processPayment(@RequestBody Map<String, String> request) {

		Map<String, Object> response = new HashMap<>();

		try {
			String clientIP = "0:0:0:0:0:0:0:1";

			request.put("public_key", publicKey);
			request.put("terNO", terNo);

			request.put("webhook_url", path_webhook);
			request.put("return_url", path_return);
			request.put("checkout_url", path_checkout);

			request.put("bill_state", "SG");
			request.put("bill_country", "SG");

			request.put("mop", "dc");
			request.put("reference", "23120228");

			request.put("bill_ip", clientIP);
			request.put("integration-type", "s2s");
			request.put("source", "Java-Spring-Curl-Direct-Payment");

			String jsonResponse = sendPostRequest(gatewayUrl, request);

			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.readValue(jsonResponse, Map.class);

			String error = (String)response.get("Error");
			// String order_status = (String) response.get("order_status");

			if (error != null && !error.isEmpty()) {
				String message = (String)response.get("Message");
				response.put("status", "error");
				response.put("message", message);
			}
			transactionService.saveTransaction(transactionService.converToTransactions(response));
			System.out.println("TransId:");

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// TODO: handle exception
		}

		return ResponseEntity.ok(response);
	}

	private String sendPostRequest(String urlStr, Map<String, String> dataPost) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoOutput(true);

		// Build POST data string
		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, String> entry : dataPost.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(entry.getKey()).append('=').append(entry.getValue());
		}

		// Send data
		try (OutputStream os = conn.getOutputStream()) {
			os.write(postData.toString().getBytes());
		}

		// Read response
		try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			String responseLine;
			StringBuilder responseBuilder = new StringBuilder();
			while ((responseLine = in.readLine()) != null) {
				responseBuilder.append(responseLine);
			}
			return responseBuilder.toString();
		}
	}

	private void handleResponse(String jsonResponse, HttpServletResponse response) throws IOException {
		try {
			// Parse the JSON response using Jackson
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(jsonResponse);

			if (jsonNode.has("authurl")) {
				String authUrl = jsonNode.get("authurl").asText();
				// Redirect to the auth URL
				response.sendRedirect(authUrl);
			} else if (jsonNode.has("error")) {
				String error = jsonNode.get("error").asText();
				response.getWriter().write("Error in Gateway Response: " + error);
			} else if (jsonNode.has("order_status")) {
				int orderStatus = jsonNode.get("order_status").asInt();
				response.getWriter().write("Order Status: " + orderStatus);
			} else {
				response.getWriter().write("Unknown Response: " + jsonResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("Error parsing response: " + e.getMessage());
		}
	}

	@PostMapping("/payment/inseert/deposit")
	public ResponseEntity<?> insertDeposit(@RequestBody Map<String, String> request) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(request);
			Deposits deposit = mapper.readValue(json, Deposits.class);
			deposit = depositsRepository.save(deposit);
			return ResponseEntity.ok(deposit);
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
