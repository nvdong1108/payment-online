package com.demo.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.entity.Settings;
import com.demo.repository.SettingsRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;
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
        try {

            log.info("Request: {}", request);
            String emailReceive = (String) request.get("emailReceive");
            String publicKey = (String) request.get("publicKey");
            String terNo = (String) request.get("terNo");

            Settings settings = settingsRepository.findById(1L).orElseThrow(() -> new RuntimeException());
            settings.setId(1L);
            settings.setEmailReceive(emailReceive);
            settings.setPublicKey(publicKey);
            settings.setTerNo(terNo);
            settings.setUpDateTime(new Date());

            settingsRepository.save(settings);
            return ResponseEntity.ok().body(settings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
