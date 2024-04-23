package com.imechanic.backend.project.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationLoginDTORequest(@NotBlank String correoElectronico,
                                            @NotBlank String contrasenia) {
}

