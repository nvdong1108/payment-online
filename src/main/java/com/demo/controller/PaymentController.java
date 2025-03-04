package com.demo.controller;

import com.demo.config.JwtUtil;
import com.demo.entity.CustomUserDetails;
import com.demo.entity.User;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
public class PaymentController {


	@Value("${gw.paywb.co.publicKey}")
	String publicKey;

	@Value("${gw.paywb.co.terNo}")
	String terNo;



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
	public String checkout(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null && authentication.getPrincipal() instanceof CustomUserDetails){
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			User user = userDetails.getUser();

			model.addAttribute("fullname",user.getFullname());
			model.addAttribute("email",user.getEmail());
			model.addAttribute("phone",user.getPhone());

			model.addAttribute("publicKey",publicKey);
			model.addAttribute("terNo",terNo);
		}
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