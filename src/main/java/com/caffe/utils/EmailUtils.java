package com.caffe.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

// ########################## EMAIL SENDER ##########################

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;

    // Create email
    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("deshitha0marketing@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        if (list != null && list.size() > 0)
            message.setCc(getCcArray(list));
        emailSender.send(message);

        message.setCc(getCcArray(list));
    }

    // Add email to list
    private String[] getCcArray(List<String> ccList) {
        String[] cc = new String[ccList.size()];
        for (int i = 0; i < ccList.size(); i++) {
            cc[i] = ccList.get(i);
        }
        return cc;
    }

    // Send forgot email
    public void forgotMail(String to, String subject, String password) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("deshitha0marketing@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg = "<p><b>Your Login details for Cafe Menagement System</b><br><b>Email:</b> " + to + "<br><b>Password: </b>" + password + "<br><a href=\"http://localhost:4200/\">Click here to login</p>";

        message.setContent(htmlMsg,"text/html");
        emailSender.send(message);
    }
}
