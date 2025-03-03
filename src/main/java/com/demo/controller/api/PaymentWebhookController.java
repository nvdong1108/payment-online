package com.demo.controller.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentWebhookController {

	@PostMapping("/webhook")
	public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
		log.info("Received Webhook Data: " + payload);	
		//
		String orderId = (String) payload.get("reference");
		String status = (String) payload.get("status");

		if ("APPROVED".equals(status)) {
			System.out.println("Order " + orderId + " has been paid successfully.");
		} else {
			System.out.println("Payment failed for Order " + orderId);
		}

		return ResponseEntity.ok("Webhook received successfully");
	}
}