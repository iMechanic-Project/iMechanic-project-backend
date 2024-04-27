package com.imechanic.backend.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MecanicoDTORequest {
    private String nombre;
    private String correoElectronico;
    private String contrasenia;
    private List<Long> servicioIds;
}
