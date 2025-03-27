package com.demo.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TemplateMailUtil {

    public static String notificationTransaction() {
        return getTemplate("transfer.html");
    }

    public static String resetPassword() {
        return getTemplate("reset-password.html");
    }

    private static String getTemplate(String templateName) {

        ClassLoader classLoader = TemplateMailUtil.class.getClassLoader();

        File file = new File(classLoader.getResource("static/formEmail/" + templateName).getFile());
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException esss) {

        }
        return content.toString();
    }

}
