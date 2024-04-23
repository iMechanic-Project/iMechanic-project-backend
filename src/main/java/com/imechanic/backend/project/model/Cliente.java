package com.imechanic.backend.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente extends Cuenta {

    @NotBlank(message = "El campo 'DNI' es obligatorio")
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe contener solo 8 números")
    private String dni;

    @NotBlank(message = "El campo 'foto' es obligatorio")
    private String foto;
}