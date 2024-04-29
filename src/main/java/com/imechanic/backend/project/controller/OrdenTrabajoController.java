package com.imechanic.backend.project.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.OrdenTrabajoDTORequest;
import com.imechanic.backend.project.controller.dto.OrdenTrabajoDTOResponse;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import com.imechanic.backend.project.service.OrdenTrabajoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orden-trabajo")
@RequiredArgsConstructor
public class OrdenTrabajoController {
    private final OrdenTrabajoService ordenTrabajoService;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @PostMapping("/crear")
    public ResponseEntity<OrdenTrabajoDTOResponse> crearOrden(@RequestBody OrdenTrabajoDTORequest ordenTrabajoDTORequest, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        return ResponseEntity.ok(ordenTrabajoService.crearOrden(decodedJWT, ordenTrabajoDTORequest));
    }
}
