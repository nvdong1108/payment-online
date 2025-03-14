package com.demo.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

//
//    @PostMapping("/token")
//    ResponseEntity<?> authenticate(@RequestBody Member member) {
//        Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());
//        if (!optionalMember.isPresent()) {
//            return ResponseEntity.badRequest().body("User not existed");
//        }
//        String token = JwtUtil.generateToken(member);
//        Map<String,Object> response = new HashMap<>();
//        response.put("token",token);
//
//        return ResponseEntity.ok(response);
//    }

}
