package com.imechanic.backend.project.controller.dto;

import com.imechanic.backend.project.enumeration.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoDTORequest {
    private String placa;
    private Long marcaId;
    private Long modeloId;
    private Categoria categoria;
}
