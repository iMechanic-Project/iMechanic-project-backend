package com.imechanic.backend.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenTrabajoMecanicoDTOList {
    private Long id;
    private String placa;
    private String fechaRegistro;
    private String horaRegistro;
    private String estado;
}