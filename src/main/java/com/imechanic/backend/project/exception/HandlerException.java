package com.imechanic.backend.project.exception;

import com.imechanic.backend.project.exception.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(TokenNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDTO> handleTokenNotFound(TokenNotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorDTO.builder()
                        .message(ex.getMessage())
                        .error("Token no válido")
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @ExceptionHandler(CredencialesIncorrectas.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDTO> handleCredencialesIncorrectas(CredencialesIncorrectas ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorDTO.builder()
                        .message(ex.getMessage())
                        .error("Credenciales invalidas")
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .build());
    }
}
