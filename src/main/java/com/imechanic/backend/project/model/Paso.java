package com.imechanic.backend.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "paso")
public class Paso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo 'paso' es obligatorio")
    private String nombre;

    private int orden;

    private boolean completado;

    @ManyToOne(targetEntity = Servicio.class)
    @JsonIgnore
    private Servicio servicio;
}
