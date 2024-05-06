package com.imechanic.backend.project.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.OrderDetailDTO;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import com.imechanic.backend.project.service.TallerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taller")
@RequiredArgsConstructor
public class TallerController {
    private final TallerService tallerService;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @GetMapping("/order-detail/{orderId}")
    public ResponseEntity<OrderDetailDTO> detalleOrdenClienteService(@PathVariable Long orderId, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return ResponseEntity.ok(tallerService.obtenerDetalleOrden(orderId, decodedJWT));
    }
}
