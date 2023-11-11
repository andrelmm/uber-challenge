package com.github.andrelmm.uberchallenge.service;

public interface IMailService {
    void sendEmail(String to, String subject, String body);
}
