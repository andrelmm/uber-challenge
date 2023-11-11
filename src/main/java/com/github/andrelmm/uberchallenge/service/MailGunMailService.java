package com.github.andrelmm.uberchallenge.service;

import com.github.andrelmm.uberchallenge.exception.MailgunException;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class MailGunMailService implements IMailService {

    @Value("${mailgun.api.key}")
    private String mailgunApiKey;

    @Value("${mailgun.domain}")
    private String mailgunDomain;

    public MailGunMailService() {}

    @Override
    @Retryable(
            maxAttemptsExpression = "${spring.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${spring.retry.backoff.delay}")
    )
    public void sendEmail(final String to,final String subject, final String body) {

        final MailgunMessagesApi mailgunMessagesApi = MailgunClient.config(mailgunApiKey)
                .createApi(MailgunMessagesApi.class);

        final Message message = Message.builder()
                .from("Andre Mello <andre@andremello.dev>")
                .to("test@mail.com")
                .subject("Hey!")
                .text("Testing sending email using Mailgun!")
                .build();

        try {
            mailgunMessagesApi.sendMessage(mailgunDomain, message);
        } catch (Exception e) {
            throw new MailgunException("Error sending email using Mailgun ", e);
        }
    }
}
