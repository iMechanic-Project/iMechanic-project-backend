package com.imechanic.backend.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    private String nombreTaller;
    private String direccionTaller;
    private String telefonoTaller;
    private List<ServicioDetalleDTO> servicios;
}
