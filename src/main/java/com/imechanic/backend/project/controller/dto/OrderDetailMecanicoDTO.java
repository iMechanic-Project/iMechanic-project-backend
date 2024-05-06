package com.imechanic.backend.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailMecanicoDTO {
    private String nombre;
    private String direccion;
    private String telefono;
    private String servicio;
    private String estadoServicio;
    private String nombreMecanico;
    private List<PasoDTO> pasos;
}
