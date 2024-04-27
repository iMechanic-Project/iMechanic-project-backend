package com.imechanic.backend.project.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import com.imechanic.backend.project.service.ServicioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/servicio")
@RequiredArgsConstructor
public class ServicioController {
    private final ServicioService servicioService;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @PutMapping("/add")
    public ResponseEntity<?> agregarServiciosATaller(@RequestBody List<Long> serviciosIds, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        try {
            servicioService.agregarServiciosATaller(decodedJWT, serviciosIds);
            return ResponseEntity.ok().build();
        } catch (EntidadNoEncontrada e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}