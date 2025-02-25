package com.demo.controller.api;


import com.demo.model.Member;
import com.demo.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiSignUpController {

    @Autowired
    private MemberService memberService;


    @PostMapping("/signup")
    public ResponseEntity<Member> signup(@RequestBody Map<String,Object> request) {
        ObjectMapper mapper = new ObjectMapper();
        Member member = new Member();
        try {
            String json = mapper.writeValueAsString(request);
            member = mapper.readValue(json,Member.class);
            Member createdMember = memberService.createMember(member);
            return ResponseEntity.ok(createdMember);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(member);
        }
    }
}
