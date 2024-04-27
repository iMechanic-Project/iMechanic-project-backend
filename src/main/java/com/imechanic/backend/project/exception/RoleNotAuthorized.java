package com.imechanic.backend.project.exception;

public class RoleNotAuthorized extends RuntimeException {
    public RoleNotAuthorized(String message) {
        super(message);
    }
}
