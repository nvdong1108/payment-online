package com.demo.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/login")
@Controller
public class LoginController {

    @GetMapping("")
    public String signUpPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(@RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "message", required = false) String message,
            Model model) {
        model.addAttribute("status", status);
        model.addAttribute("message", message);
        return "login/forgot-password";
    }

}
