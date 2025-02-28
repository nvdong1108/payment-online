package com.demo.controller;

import com.demo.config.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

@Slf4j
@Controller
public class PaymentController {


	@GetMapping("/payment")
	public String payment(@RequestHeader(value = "Authorization", required = false) String token, Model model) {

		if(token == null  && token.isEmpty()){
			return "redirect:/login";
		}
		String jwt = token.substring(7);
		if (!JwtUtil.validateToken(jwt)) {
			return "redirect:/login";
		}

		String username = JwtUtil.getUsernameFromToken(jwt);
		model.addAttribute("username", username);
		return "payment";
	}


	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/checkout")
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