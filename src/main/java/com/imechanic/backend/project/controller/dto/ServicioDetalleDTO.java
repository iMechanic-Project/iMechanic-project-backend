package com.imechanic.backend.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioDetalleDTO {
    private ServicioDTO servicio;
    private MecanicoDTOList mecanico;
    private String estadoServicio;
    private List<PasoDTO> pasos;
}