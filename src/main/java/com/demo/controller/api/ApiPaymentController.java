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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.service.EmailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiPaymentController {

	
	private String gatewayUrl = "https://gw.paywb.co/directapi";
	private String path_webhook = "http://localhost:8090/api/payment/webhook";
	private String path_return = "http://localhost:8090/payment/return";
	private String path_checkout = "http://localhost:8090/checkout";
	


	@Autowired
	private EmailService emailService;	

	
	@PostMapping("/payment/sendEmail")
	public ResponseEntity<?> sendEmail(@RequestBody Map<String, String> request) {

		String to = request.get("to");	
		String subject = request.get("subject");
		String body = request.get("body");
		
		try {
			emailService.sendEmail(to, subject, body);
			return ResponseEntity.ok("Email sent successfully");
		} catch (MessagingException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}


	
	@PostMapping("/payment/checkout")
	public ResponseEntity<?> processPayment(@RequestBody Map<String, String> request) {

		Map<String, String> response = new HashMap<>();
		
		String terNO = "816";
		String public_key= "MTEzMTFfODE2XzIwMjQxMjExMTMwNTQ4"; // 816 - test
		// String terNO = "916";
		// String public_key= "MTE3MDBfOTE3XzIwMjUwMjIzMTUzMjUw"; // 917

		// String public_key= "MTE2OThfOTE2XzlwMjUwMjlzMTlwNTAy"; // 916
		
		try {
			String clientIP = "0:0:0:0:0:0:0:1";
			
			request.put("public_key", public_key);
			request.put("terNO", terNO);
			
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
			
			String jsonResponse =sendPostRequest(gatewayUrl, request);
			
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.readValue(jsonResponse, Map.class);
			
			String error = response.get("Error");
			String order_status = (String)response.get("order_status");
			
			if(error != null && !error.isEmpty()) {
				String message =response.get("Message");
				response.put("status", "error");
				response.put("message", message);
			}
			
			System.out.println("TransId:");
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			
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
            if (postData.length() != 0) postData.append('&');
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

}
