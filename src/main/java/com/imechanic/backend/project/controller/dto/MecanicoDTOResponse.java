package com.imechanic.backend.project.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"message"})
public record MecanicoDTOResponse(String message) {
}

