package com.demo.service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.demo.common.SendMailUtil;
import com.demo.entity.Settings;
import com.demo.repository.SettingsRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    SettingsRepository settingsRepository;

    public void sendEmail(Map<String, Object> map) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        Optional<Settings> settings = settingsRepository.findById(1L);
        if (!settings.isPresent()) {
            log.error("Setting not found");
            return;
        }
        Settings setting = settings.get();
        String from = setting.getEmailSend();
        String ccMail = setting.getEmailReceive();
        String subject = "TRANSACTION NOTIFICATION";

        String toMail = (String) map.get("toMail");
        String username = (String) map.get("username");
        String amount = (String) map.get("amount");
        String status = (String) map.get("status");


        String body = SendMailUtil.readTemplateMail();
        body = body.replace("[User Name]", username);
        body = body.replace("[Amount]", amount);
        body = body.replace("[Status]", status);

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo(toMail);
        helper.setCc(ccMail);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }

}
