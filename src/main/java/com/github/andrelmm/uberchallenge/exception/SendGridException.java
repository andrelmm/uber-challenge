package com.github.andrelmm.uberchallenge.exception;

public class SendGridException extends RuntimeException {
    public SendGridException(String message, Throwable cause) {
        super(message, cause);
    }
}