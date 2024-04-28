package com.imechanic.backend.project.model;

import com.imechanic.backend.project.enumeration.Categoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehiculo")
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo 'placa' es obligatorio")
    @Column(unique = true)
    private String placa;

    @ManyToOne
    private Marca marca;

    @ManyToOne
    private Modelo modelo;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne(targetEntity = Cuenta.class)
    private Cuenta cuenta;
}
