package com.demo.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.demo.common.TemplateMailUtil;
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

    public void sendLinkResetPassword(String username, String toMail) throws MessagingException {
        String subject = "Password Reset Request";


        String body = TemplateMailUtil.resetPassword();
        body = body.replace("{{username}}", username);

        sendEmail(toMail, subject, body);

    }

    public void sendEmailTransaction(Map<String, Object> map) throws MessagingException {

        String subject = "Transaction notify";

        String toMail = (String) map.get("toMail");
        String username = (String) map.get("username");
        String amount = (String) map.get("amount");
        String status = (String) map.get("status");

        String body = TemplateMailUtil.notificationTransaction();
        body = body.replace("[User Name]", username);
        body = body.replace("[Amount]", amount);
        body = body.replace("[Status]", status);

        sendEmail(toMail, subject, body);
    }

    private void sendEmail(String toMail, String subject, String body) throws MessagingException {

        Optional<Settings> settings = settingsRepository.findById(1L);
        if (!settings.isPresent()) {
            log.error("Setting not found");
            return;
        }

        Settings setting = settings.get();
        String from = setting.getEmailSend();
        String ccMail = setting.getEmailReceive();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(toMail);
        helper.setCc(ccMail);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
        // Implement your email sending logic here
    }

}
