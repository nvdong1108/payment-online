package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.entity.Settings;
import com.demo.repository.SettingsRepository;

@Controller
public class SettingController {

    @Autowired
    SettingsRepository settingRepository;

    @GetMapping("/settings")
    public String getMethodName(Model model) {

        Settings setting = new Settings();
        List<Settings> listSettings = settingRepository.findAll();
        if (listSettings != null && !listSettings.isEmpty()) {
            setting = listSettings.get(0);
        }
        
        String email_send = setting.getEmailSend();
        String password_send = setting.getPasswordSend();
        String email_receive = setting.getEmailReceive();
        String public_key = setting.getPublicKey();
        String ter_no = setting.getTerNo();

        model.addAttribute("emailSend", email_send);
        model.addAttribute("passwordSend", password_send);
        model.addAttribute("emailReceive", email_receive);
        model.addAttribute("publicKey", public_key);
        model.addAttribute("terNo", ter_no);

        return "settings";
    }
}
