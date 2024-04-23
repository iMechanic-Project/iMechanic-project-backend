package com.imechanic.backend.project.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "jwt", "status"})
public record LoginDTOResponse(String username,
                               String message,
                               String jwt,
                               boolean status) {
}
