package com.imechanic.backend.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MecanicoPasoDTO {
    private Long ordenTrabajoId;
    private Long mecanicoId;
    private Long servicioId;
    private String servicioNombre;
    private Long pasoId;
    private boolean complete;
}

