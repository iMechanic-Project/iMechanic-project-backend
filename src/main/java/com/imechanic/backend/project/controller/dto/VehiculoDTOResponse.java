package com.imechanic.backend.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoDTOResponse {
    private String placa;
    private String marca;
    private String modelo;
    private String categoria;
}
