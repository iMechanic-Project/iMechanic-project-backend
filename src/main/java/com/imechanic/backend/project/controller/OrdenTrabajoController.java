package com.imechanic.backend.project.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.CreateOrdenDTORequest;
import com.imechanic.backend.project.controller.dto.OrdenTrabajoDTOList;
import com.imechanic.backend.project.controller.dto.ServicioMecanicoDTO;
import com.imechanic.backend.project.controller.dto.VehiculoSearchDTOResponse;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import com.imechanic.backend.project.service.OrdenTrabajoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orden")
@RequiredArgsConstructor
public class OrdenTrabajoController {
    private final OrdenTrabajoService ordenTrabajoService;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @PostMapping("/crear")
    public ResponseEntity<VehiculoSearchDTOResponse> crearOrden(@RequestBody CreateOrdenDTORequest createOrdenDTORequest, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        return ResponseEntity.ok(ordenTrabajoService.crearOrden(decodedJWT, createOrdenDTORequest));
    }

    @GetMapping("/todas")
    public ResponseEntity<List<OrdenTrabajoDTOList>> obtenerOrdenesDeTaller(HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        List<OrdenTrabajoDTOList> ordenes = ordenTrabajoService.obtenerTodasLasOrdenesDeTaller(decodedJWT);
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/todas/servicios-mecanico")
    public ResponseEntity<List<ServicioMecanicoDTO>> obtenerServicioMecanicoDeTaller(HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        List<ServicioMecanicoDTO> ordenes = ordenTrabajoService.obtenerMecanicoServicioDeTaller(decodedJWT);
        return ResponseEntity.ok(ordenes);
    }
}