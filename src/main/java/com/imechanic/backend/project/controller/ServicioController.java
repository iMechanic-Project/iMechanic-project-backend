package com.imechanic.backend.project.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.ServicioDTO;
import com.imechanic.backend.project.enumeration.TipoServicio;
import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.model.Servicio;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import com.imechanic.backend.project.service.ServicioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicio")
@RequiredArgsConstructor
public class ServicioController {
    private final ServicioService servicioService;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @GetMapping("/mantenimiento")
    public ResponseEntity<List<ServicioDTO>> obtenerServiciosDeMantenimiento(HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        try {
            List<ServicioDTO> servicios = servicioService.obtenerServiciosPorTipo(TipoServicio.MANTENIMIENTO, decodedJWT);
            return ResponseEntity.ok(servicios);
        } catch (EntidadNoEncontrada e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/reparacion")
    public ResponseEntity<List<ServicioDTO>> obtenerServiciosDeReparacion(HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        try {
            List<ServicioDTO> servicios = servicioService.obtenerServiciosPorTipo(TipoServicio.REPARACION, decodedJWT);
            return ResponseEntity.ok(servicios);
        } catch (EntidadNoEncontrada e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServicioDTO>> obtenerMisServicios(HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        try {
            List<ServicioDTO> servicios = servicioService.obtenerMisServicio(decodedJWT);
            return ResponseEntity.ok(servicios);
        } catch (EntidadNoEncontrada e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/add")
    public ResponseEntity<List<Servicio>> agregarServiciosATaller(@RequestBody List<Long> serviciosIds, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        try {
            return ResponseEntity.ok(servicioService.agregarServiciosATaller(decodedJWT, serviciosIds));
        } catch (EntidadNoEncontrada e) {
            return ResponseEntity.notFound().build();
        }
    }
}