package com.imechanic.backend.project.exception;

import com.imechanic.backend.project.exception.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

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
                        .date(new Date())
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
                        .date(new Date())
                        .build());
    }

    @ExceptionHandler(JwtAuthentication.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDTO> handleJwtAuthentication(JwtAuthentication ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorDTO.builder()
                        .message(ex.getMessage())
                        .error("Entidad no encontrada")
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .date(new Date())
                        .build());
    }

    @ExceptionHandler(RoleNotAuthorized.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorDTO> handleRoleNotAuthorized(RoleNotAuthorized ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorDTO.builder()
                        .message(ex.getMessage())
                        .error("Role no autorizado")
                        .status(HttpStatus.FORBIDDEN.value())
                        .date(new Date())
                        .build());
    }

    @ExceptionHandler(PasoYaCompletado.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorDTO> handlePasoYaCompletado(PasoYaCompletado ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorDTO.builder()
                        .message(ex.getMessage())
                        .error("Paso ya completado")
                        .status(HttpStatus.FORBIDDEN.value())
                        .date(new Date())
                        .build());
    }
}