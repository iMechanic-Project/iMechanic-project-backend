package com.imechanic.backend.project.controller;

import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicio")
@RequiredArgsConstructor
public class ServicioController {
    private final ServicioService servicioService;

    @PutMapping("/{tallerId}/agregar-servicios")
    public ResponseEntity<?> agregarServiciosATaller(@PathVariable Long tallerId, @RequestBody List<Long> serviciosIds) {
        try {
            servicioService.agregarServiciosATaller(tallerId, serviciosIds);
            return ResponseEntity.ok().build();
        } catch (EntidadNoEncontrada e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}