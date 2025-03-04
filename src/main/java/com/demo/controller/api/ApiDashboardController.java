package com.demo.controller.api;

import com.demo.config.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiDashboardController {



   @GetMapping("/dashboard")
   public String dashboard(@RequestHeader(value = "Authorization", required = false) String token,
                           HttpServletRequest request,
                           Model model) {
       if (token == null || !token.startsWith("Bearer ")) {
           return "redirect:/login";
       }

       String jwt = token.substring(7);
       if (!JwtUtil.validateToken(jwt)) {
           return "redirect:/login";
       }

       String username = JwtUtil.getUsernameFromToken(jwt);
       model.addAttribute("username", username);
       return "dashboard";
   }

}