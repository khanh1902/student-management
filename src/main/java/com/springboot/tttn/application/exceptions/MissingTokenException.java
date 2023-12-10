package com.springbot.tttn.application.exceptions;

public class MissingTokenException extends RuntimeException{
    public MissingTokenException(String message) {
        super(message);
    }
}
