package com.demo.controller.api;


import com.demo.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/api")
public class ApiLoginController {


    @Autowired
    private CustomUserDetailsService  customUserDetailsService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> request) {

        Map<String,Object> response = new HashMap<>();
        String email = request.get("email").toString();
        String password = request.get("password").toString();
     
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error(ex.getMessage());
           return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }
}
