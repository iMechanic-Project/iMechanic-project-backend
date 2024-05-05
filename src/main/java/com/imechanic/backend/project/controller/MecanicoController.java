package com.imechanic.backend.project.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.*;
import com.imechanic.backend.project.exception.RoleNotAuthorized;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import com.imechanic.backend.project.service.MecanicoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mecanico")
@RequiredArgsConstructor
public class MecanicoController {
    private final MecanicoService mecanicoService;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @GetMapping("/all")
    public ResponseEntity<List<MecanicoDTO>> getAllMechanics(HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        try {
            List<MecanicoDTO> mecanicos = mecanicoService.getAllMechanicsByTaller(decodedJWT);
            return ResponseEntity.ok(mecanicos);
        } catch (RoleNotAuthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<MecanicoDTOList>> getMechanicsByService(HttpServletRequest request, @PathVariable Long serviceId) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        List<MecanicoDTOList> mechanics = mecanicoService.getAllMechanicsByService(decodedJWT, serviceId);
        return ResponseEntity.ok(mechanics);
    }

    @PostMapping("/crear")
    public ResponseEntity<MecanicoDTOResponse> crearMecanico(@Valid @RequestBody MecanicoDTORequest mecanico, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return new ResponseEntity<>(mecanicoService.createMecanico(decodedJWT, mecanico), HttpStatus.CREATED);
    }

    @PutMapping("/iniciar-orden/{orderId}")
    public ResponseEntity<String> iniciarServicio(HttpServletRequest request, @PathVariable Long orderId) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return ResponseEntity.ok(mecanicoService.iniciarServicioOrden(decodedJWT, orderId));
    }

    @PutMapping("/service/{serviceId}/paso/{pasoId}/complete")
    public ResponseEntity<?> completarPaso(@PathVariable Long serviceId, @PathVariable Long pasoId, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return ResponseEntity.ok(mecanicoService.completarPaso(serviceId, pasoId, decodedJWT));
    }

    @GetMapping("/order-detail/{orderId}")
    public ResponseEntity<OrderDetailMecanicoDTO> detalleOrdenmMecanicoService(@PathVariable Long orderId, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return ResponseEntity.ok(mecanicoService.obtenerDetalleOrden(orderId, decodedJWT));
    }

    @PutMapping("/terminar-orden/{orderId}")
    public ResponseEntity<String> terminarServicio(HttpServletRequest request, @PathVariable Long orderId) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return ResponseEntity.ok(mecanicoService.terminarServicioOrden(decodedJWT, orderId));
    }
}
