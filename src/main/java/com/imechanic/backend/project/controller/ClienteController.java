package com.imechanic.backend.project.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.OrdenTrabajoClienteDTOList;
import com.imechanic.backend.project.controller.dto.OrderDetailDTO;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import com.imechanic.backend.project.service.ClienteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @GetMapping("/ordenes")
    public ResponseEntity<List<OrdenTrabajoClienteDTOList>> obtenerOrdenesDeTaller(HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        List<OrdenTrabajoClienteDTOList> ordenes = clienteService.obtenerTodasLasOrdenesDeCliente(decodedJWT);
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/order-detail/{orderId}")
    public ResponseEntity<OrderDetailDTO> detalleOrdenClienteService(@PathVariable Long orderId, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);
        return ResponseEntity.ok(clienteService.obtenerDetalleOrden(orderId, decodedJWT));
    }
}
