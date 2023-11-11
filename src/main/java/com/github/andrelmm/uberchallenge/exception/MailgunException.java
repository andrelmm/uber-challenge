package com.github.andrelmm.uberchallenge.exception;

public class MailgunException extends RuntimeException {
    public MailgunException(String message, Throwable cause) {
        super(message, cause);
    }
}