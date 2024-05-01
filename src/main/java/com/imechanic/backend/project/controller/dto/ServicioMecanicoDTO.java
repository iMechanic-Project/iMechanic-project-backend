package com.imechanic.backend.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioMecanicoDTO {
    private Long servicioId;
    private Long mecanicoId;
}

