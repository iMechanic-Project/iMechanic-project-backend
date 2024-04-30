package com.imechanic.backend.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MecanicoDTO {
    private String nombre;
    private String correoElectronico;
    private List<ServicioDTO> servicios;
}

