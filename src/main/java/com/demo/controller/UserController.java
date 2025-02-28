package com.demo.controller;


import com.demo.dto.request.ApiResponse;
import com.demo.dto.response.UserResponse;
import com.demo.entity.User;
import com.demo.repository.UserRepository;
import com.demo.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping
    ResponseEntity<User> createUser(@RequestBody User user) {
         user= userRepository.save(user);
        return ResponseEntity.ok(user);
    }

}
