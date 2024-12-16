package com.ecommerce.nunda.configs;


import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailGunConfig {

    @Value("${mailgun.api.key}")
    private String PRIVATE_API_KEY;

    @Value("${mailgun.domain}")
    private String domain;

    @Bean
    public MailgunMessagesApi mailgunAsyncMessagesApi() {
        return MailgunClient.config(PRIVATE_API_KEY)
                .createAsyncApi(MailgunMessagesApi.class);
    }
}