package com.demo.controller.api;


import com.demo.model.Member;
import com.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiLoginController {


    @Autowired
    private MemberService memberService;


    private final AuthenticationManager authenticationManager;

    public ApiLoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> request) {
        Map<String, String> response = new HashMap<>();

        String email = request.get("email").toString();
        String password = request.get("password").toString();
        Optional<Member> memberOptional =  memberService.findByEmailAndPassword(email,password);
        if (memberOptional.isPresent()) {
            response.put("message", "Login successful");
            response.put("status", "success");
            response.put("redirectUrl", "/dashboard");
            return ResponseEntity.ok().body(response);
        } else {
            response.put("message", "Invalid email or password");
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
