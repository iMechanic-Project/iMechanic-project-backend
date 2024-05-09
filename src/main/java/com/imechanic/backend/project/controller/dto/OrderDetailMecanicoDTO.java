package com.imechanic.backend.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailMecanicoDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private ServicioDTO servicio;
    private String estadoServicio;
    private MecanicoDTOList mecanico;
    private List<PasoDTO> pasos;
}
