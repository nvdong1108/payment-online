package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {


    @GetMapping("/signup")
    public String signUpPage() {
        return "signup";
    }

    @GetMapping("/signup_success")
    public String signUpSuccessPage(@RequestParam("username") String username, @RequestParam("email") String email, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        return "signup_success";
    }




    
}
