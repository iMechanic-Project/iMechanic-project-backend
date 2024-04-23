package com.imechanic.backend.project.exception;

public class TokenNotFound extends RuntimeException{
    public TokenNotFound(String message) {
        super(message);
    }
}
