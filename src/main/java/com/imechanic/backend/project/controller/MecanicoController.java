package com.imechanic.backend.project.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.MecanicoDTORequest;
import com.imechanic.backend.project.controller.dto.MecanicoDTOResponse;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import com.imechanic.backend.project.service.MecanicoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mecanico")
@RequiredArgsConstructor
public class MecanicoController {
    private final MecanicoService mecanicoService;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @PostMapping("/crear")
    public ResponseEntity<MecanicoDTOResponse> crearMecanico(@Valid @RequestBody MecanicoDTORequest mecanico, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return new ResponseEntity<>(mecanicoService.createMecanico(decodedJWT, mecanico), HttpStatus.CREATED);
    }
}
