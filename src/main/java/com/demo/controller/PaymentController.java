package com.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;


@Controller
public class PaymentController {






	@GetMapping("/payment")
	public String payment() {
		return "payment";
	}


	@GetMapping("/checkout")
	@PreAuthorize("hasRole('USER')")
	public String checkout(HttpServletRequest request,Model model) {
		return "checkout";
	}
	
	
	@GetMapping("/payment/return")
    public String returnPage(@RequestParam("reference") String orderId, 
                             @RequestParam("status") String status, Model model) {
        model.addAttribute("orderId", orderId);
        model.addAttribute("status", status);
        return "payment_result"; 
    }

}