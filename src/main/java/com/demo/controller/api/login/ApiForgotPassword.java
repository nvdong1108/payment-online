package com.demo.controller.api.login;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.common.TemplateMailUtil;
import com.demo.entity.User;
import com.demo.service.EmailService;
import com.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequestMapping("/api")
@RestController
public class ApiForgotPassword {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassworStringd(@RequestBody Map<String, Object> mapBody) {
        Map<String, Object> response = new HashMap<>();
        String status = "Ok";
        try {

            String userName = (String) mapBody.get("username");
            String email = userService.getEmailByUserName(userName);
            emailService.sendLinkResetPassword(userName, email);
            response.put("maskedEmail", maskEmail(email));
        } catch (UsernameNotFoundException | NullPointerException e) {
            log.error(e.getMessage(), e);
            status = "Error";
            response.put("message", e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            status = "Error";
            response.put("message", "The system is busy. Please try again later.");
        }
        response.put("status", status);
        return ResponseEntity.ok(response);

    }

    private String maskEmail(String email) {
        if (email == null || email.length() < 5)
            return email;
        int atIndex = email.indexOf('@');
        if (atIndex < 3)
            return email;

        String firstPart = email.substring(0, 3);
        String lastPart = email.substring(atIndex);
        return firstPart + "*****" + lastPart;
    }

}
