package com.imechanic.backend.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "marca")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campos 'Marca' es obligatorio")
    private String nombre;

    @OneToMany(targetEntity = Modelo.class, fetch = FetchType.LAZY, mappedBy = "marca")
    @JsonIgnore
    private List<Modelo> modelos;
}
