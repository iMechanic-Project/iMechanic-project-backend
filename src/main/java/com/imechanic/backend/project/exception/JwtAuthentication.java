package com.imechanic.backend.project.exception;

public class JwtAuthentication extends RuntimeException {
    public JwtAuthentication(String message) {
        super(message);
    }
}
