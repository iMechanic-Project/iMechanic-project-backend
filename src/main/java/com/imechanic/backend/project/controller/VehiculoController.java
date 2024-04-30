package com.imechanic.backend.project.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.VehiculoDTORequest;
import com.imechanic.backend.project.controller.dto.VehiculoDTOResponse;
import com.imechanic.backend.project.model.Marca;
import com.imechanic.backend.project.model.Modelo;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import com.imechanic.backend.project.service.VehiculoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehiculo")
@RequiredArgsConstructor
public class VehiculoController {
    private final VehiculoService vehiculoService;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @GetMapping("/all")
    public ResponseEntity<List<VehiculoDTOResponse>> getAllVehicles(HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        List<VehiculoDTOResponse> vehiculos = vehiculoService.obtenerMisVehiculos(decodedJWT);
        return ResponseEntity.ok(vehiculos);
    }


    @GetMapping("/marcas")
    public ResponseEntity<List<Marca>> todasLasMarcas() {
        return ResponseEntity.ok(vehiculoService.obtenerTodasLasMarcas());
    }

    @GetMapping("/modelos/{marcaId}")
    public ResponseEntity<List<Modelo>> todosLosModelos(@PathVariable Long marcaId) {
        return ResponseEntity.ok(vehiculoService.obtenerTodosLosModelosDeLaMarca(marcaId));
    }

    @PostMapping("/crear")
    public ResponseEntity<VehiculoDTOResponse> crearVehiculo(@RequestBody VehiculoDTORequest vehiculoDTORequest, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtAuthenticationManager.validateToken(request);

        VehiculoDTOResponse response = vehiculoService.crearVehiculo(vehiculoDTORequest, decodedJWT);
        return ResponseEntity.ok(response);
    }
}
