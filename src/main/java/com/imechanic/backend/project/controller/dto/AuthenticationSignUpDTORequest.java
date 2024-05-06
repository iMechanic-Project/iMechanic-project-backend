package com.imechanic.backend.project.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationSignUpDTORequest(@NotBlank String correoElectronico,
                                             @NotBlank String contrasenia,
                                             @NotBlank String nombre,
                                             @NotBlank String telefono,
                                             @NotBlank String direccion,
                                             @NotBlank String role) {

}