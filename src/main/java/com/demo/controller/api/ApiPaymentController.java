package com.demo.controller.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.common.StringUtil;
import com.demo.entity.CustomUserDetails;
import com.demo.entity.Deposits;
import com.demo.entity.Settings;
import com.demo.entity.Transactions;
import com.demo.entity.User;
import com.demo.repository.DepositsRepository;
import com.demo.repository.SettingsRepository;
import com.demo.service.EmailService;
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
	private String path_webhook = "http://payworknet.biz/api/payment/webhook";
	private String path_return = "http://payworknet.biz/payment/return";
	private String path_checkout = "http://payworknet.biz/checkout";

	@Autowired
	private DepositsRepository depositsRepository;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private EmailService emailService;


	@Autowired
	private SettingsRepository settingsRepository;


	@PostMapping("/payment/checkout")
	public ResponseEntity<?> processPayment(@RequestBody Map<String, String> request) {

		Map<String, Object> response = new HashMap<>();
		Optional<Settings> settings = settingsRepository.findById(1L);
		String publicKey = null;
		String terNo = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user =  null;
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			user = userDetails.getUser();
		}
		if (settings.isPresent()) {
			Settings setting = settings.get();
			publicKey = setting.getPublicKey();
			terNo = setting.getTerNo();
		}

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

			String note = request.get("notes");


			String jsonResponse = sendPostRequest(gatewayUrl, request);

			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.readValue(jsonResponse, Map.class);

			String error = StringUtil.converToString(response.get("Error"));
			if (error != null && !error.isEmpty()) {
				String message = (String) response.get("Message");
				response.put("status", "error");
				response.put("message", message);
				return ResponseEntity.ok(response);
			}
			
			response.put("status", "success");
			Transactions transaction = transactionService.converToTransactions(response);
			transaction.setUsername(user.getUsername());
			transaction.setNote(note);
			
			transactionService.saveTransaction(transaction);
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("username", user.getUsername());
				map.put("toMail", user.getEmail());
				map.put("amount", response.get("bill_amt"));
				map.put("status", response.get("status"));
				emailService.sendEmailTransaction(map);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.put("status", "error");
			response.put("message", "Error processing payment.");
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