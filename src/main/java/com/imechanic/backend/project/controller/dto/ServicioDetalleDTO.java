package com.imechanic.backend.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioDetalleDTO {
    private String nombreServicio;
    private String nombreMecanico;
    private String estadoServicio;
    private List<String> pasos;
}