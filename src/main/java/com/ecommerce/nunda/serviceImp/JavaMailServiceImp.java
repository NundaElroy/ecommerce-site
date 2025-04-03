package com.ecommerce.nunda.serviceImp;


import com.ecommerce.nunda.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service("javaMailService")
public class JavaMailServiceImp implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public JavaMailServiceImp(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }


    public void sendConfirmationEmail(String name, String email) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");

        String subject = "REGISTRATION CONFIRMATION";
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("email", email);
        context.setVariable("url", "dont matter");

        String body = templateEngine.process("email/registration", context);

        try {
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setTo(email);
            javaMailSender.send(message);
        }catch (Exception e){
            throw new MailSendException("Failed to send email");

        }

    }
}

