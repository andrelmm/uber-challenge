package com.github.andrelmm.uberchallenge.service;

import com.github.andrelmm.uberchallenge.exception.SendGridException;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SengGridMailService implements IMailService {
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    private final MailGunMailService mailgunEmailService;

    public SengGridMailService(MailGunMailService mailgunEmailService) {
        this.mailgunEmailService = mailgunEmailService;
    }

    @Override
    @Retryable(
            retryFor = SendGridException.class,
            maxAttemptsExpression = "${spring.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${spring.retry.backoff.delay}")
    )
    @CircuitBreaker(name = "emailService", fallbackMethod = "sendMailFallback")
    public void sendEmail(final String to,final String subject, final String body) {
        final SendGrid sendGrid = new SendGrid(sendGridApiKey);
        final Email from = new Email("your@email.com");
        final Email toEmail = new Email(to);
        final Mail mail = new Mail(from, subject, toEmail, new Content("text/plain", body));
        final Request request = new Request();
        try {
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException e) {
            throw new SendGridException("Error sending email using Sendgrid", e);
        }
    }

    // Fallback method for the Circuit Breaker
    public void sendMailFallback(final String to, final String subject, final String body, Exception exception) {
        mailgunEmailService.sendEmail(to, subject, body);
    }
}
