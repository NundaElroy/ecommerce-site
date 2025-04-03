package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.service.EmailService;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.model.message.Message;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



@Service("mailgunService")
public class EmailServiceImp implements EmailService {

    @Value("${mailgun.domain}")
    private String mailgunDomain;

    @Value("${mailgun.sender}")
    private String mailgunSender;

    @Value("${activation.url}")
    private String url;

    private final MailgunMessagesApi mailgunMessagesApi;
    private final TemplateEngine templateEngine;

    public EmailServiceImp(MailgunMessagesApi mailgunMessagesApi, TemplateEngine templateEngine) {
        this.mailgunMessagesApi = mailgunMessagesApi;
        this.templateEngine = templateEngine;
    }

    public void sendConfirmationEmail(String name, String email) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("email", email);
        context.setVariable("url", url);

        String body = templateEngine.process("email/registration", context);

        sendEmailWithMailgun(email, body);
    }

    private void sendEmailWithMailgun(String recipient, String body) {
        Message message = Message.builder()
        .from(mailgunSender)
        .to(recipient)
        .subject("REGISTRATION CONFIRMATION")
        .html(body)
                .build();

        mailgunMessagesApi.sendMessage(mailgunDomain, message);
    }
}



