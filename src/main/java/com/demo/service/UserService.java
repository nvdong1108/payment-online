package com.demo.service;


import com.demo.dto.response.UserResponse;
import com.demo.entity.User;
import com.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        User userCreate =  userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        return  user;
    }


    public String getEmailByUserName(String username)  throws UsernameNotFoundException , NullPointerException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException( "Username does not exist.");
        }
        User user = userOptional.get();
        if (user.getEmail() == null) {
            throw new NullPointerException( "User email not found.");
        }
        return user.getEmail();
    }
}
