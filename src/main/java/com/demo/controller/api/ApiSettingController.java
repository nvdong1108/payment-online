package com.demo.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.entity.Settings;
import com.demo.repository.SettingsRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Slf4j
@RestController
@RequestMapping("/api")
public class ApiSettingController {
       
    @Autowired
    private SettingsRepository settingsRepository;

    @PostMapping("/settings")
    public ResponseEntity<?> postMethodName(@RequestBody Map<String, Object> request) {
        log.info("Request: {}", request);
        // String emailSend = (String) request.get("emailSend");
        // String passwordSend = (String) request.get("passwordSend");
        String emailReceive = (String) request.get("emailReceive");
        String publicKey = (String) request.get("publicKey");
        String terNo = (String) request.get("terNo");

        Settings settings = new Settings();
        settings.setId(1L); 
        // settings.setEmailSend(emailSend);
        // settings.setPasswordSend(passwordSend);
        settings.setEmailReceive(emailReceive);
        settings.setPublicKey(publicKey);
        settings.setTerNo(terNo);
        settings.setUpDateTime(new Date());
        
        settingsRepository.save(settings);
        return ResponseEntity.ok().body(settings);
    }
    
}
