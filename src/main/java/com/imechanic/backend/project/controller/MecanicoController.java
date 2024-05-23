package com.imechanic.backend.project.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.*;
import com.imechanic.backend.project.exception.RoleNotAuthorized;
import com.imechanic.backend.project.model.Paso;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import com.imechanic.backend.project.service.MecanicoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/all/order")
    public ResponseEntity<List<MecanicoDTOList>> getAllMechanicsForOrder(HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        try {
            List<MecanicoDTOList> mecanicos = mecanicoService.getAllMechanicsByTallerForOrder(decodedJWT);
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

    @GetMapping("/ordenes")
    public ResponseEntity<List<OrdenTrabajoMecanicoDTOList>> obtenerOrdenesDeTaller(HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        List<OrdenTrabajoMecanicoDTOList> ordenes = mecanicoService.obtenerTodasLasOrdenesDeMecanico(decodedJWT);
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/order-detail/{orderId}")
    public ResponseEntity<OrderDetailMecanicoDTO> detalleOrdenMecanicoService(@PathVariable Long orderId, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return ResponseEntity.ok(mecanicoService.obtenerDetalleOrden(orderId, decodedJWT));
    }

    @PutMapping("/iniciar/{orderId}/servicio/{serviceId}")
    public ResponseEntity<Map<String, String>> iniciarServicio(HttpServletRequest request, @PathVariable Long orderId, @PathVariable Long serviceId) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return ResponseEntity.ok(mecanicoService.iniciarServicioOrden(decodedJWT, orderId, serviceId).getBody());
    }

    @PutMapping("/orden/{ordenId}/service/{serviceId}/paso/{pasoId}/complete")
    public ResponseEntity<MecanicoPasoDTO> completarPaso(HttpServletRequest request, @PathVariable Long ordenId, @PathVariable Long serviceId, @PathVariable Long pasoId) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return ResponseEntity.ok(mecanicoService.completarPaso(decodedJWT, ordenId, serviceId, pasoId));
    }

    @GetMapping("/orden/{ordenId}/service/{serviceId}/complete-list")
    public ResponseEntity<List<Paso>> getPasosCompletos(HttpServletRequest request, @PathVariable Long ordenId, @PathVariable Long serviceId) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return ResponseEntity.ok(mecanicoService.getPasosCompletados(decodedJWT, ordenId, serviceId));
    }

    @PutMapping("/terminar/{orderId}/servicio/{serviceId}")
    public ResponseEntity<Map<String, String>> terminarServicio(HttpServletRequest request, @PathVariable Long orderId, @PathVariable Long serviceId) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return ResponseEntity.ok(mecanicoService.terminarServicioOrden(decodedJWT, orderId, serviceId).getBody());
    }
}
