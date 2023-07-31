package com.example.service;

import com.example.util.EmailUtil;
import com.example.util.JWTUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${server.url}")
    private String serverUrl;

    private ExecutorService executor;


    public MailSenderService() {
    }

    public void EmailSender() {
        // Create a thread pool with a single thread for email sending
        executor = Executors.newSingleThreadExecutor();
    }


    void sendEmail(String toAccount, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromEmail);
        msg.setTo(toAccount);
        msg.setSubject(subject);
        msg.setText(text);
        javaMailSender.send(msg);
    }

    void sendMimeEmail(String toAccount, String subject, String text) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom(fromEmail);
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendEmailVerification(String toAccount,String name, Integer id) {
//        TODO add thread
        Integer counter = 1;
        Long sendTime = 0L;
        String jwt = JWTUtil.encodeEmailJwt(id);
        String url = serverUrl + "/api/v1/auth/verification/email/" + jwt;

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("<h1 style=\"text-align: center\">Hello %s</h1>",name));
        builder.append("<p>");
        builder.append(String.format("<a href=\"%s\"> Click link to complete registration </a>", url));
        builder.append("</p>");

        if(counter==1){
            sendTime= System.currentTimeMillis();
        }
        EmailUtil.timeController(sendTime);
            sendMimeEmail(toAccount, "Kun uz registration compilation", builder.toString());
        counter++;
        return builder.toString();
    }

    public void shutdown() {
        // Shutdown the thread pool when you are done with the email sending
        executor.shutdown();
    }




}
