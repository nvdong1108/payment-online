package com.demo.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

//    public static void main(String[] args) {
//        String rawPassword = "123456";
//        String encodedPassword = encodePassword(rawPassword);
//        System.out.println("Encoded Password: " + encodedPassword);
//    }

}
