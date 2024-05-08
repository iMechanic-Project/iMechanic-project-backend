package com.imechanic.backend.project.model;

import com.imechanic.backend.project.enumeration.EstadoServicio;
import com.imechanic.backend.project.enumeration.TipoServicio;
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
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo 'nombre' es obligatorio")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servicio", nullable = false)
    private TipoServicio tipoServicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_servicio", nullable = false)
    private EstadoServicio estadoServicio;

    @OneToMany(targetEntity = Paso.class, fetch = FetchType.LAZY, mappedBy = "servicio")
    private List<Paso> pasos;

    @OneToMany(mappedBy = "servicio")
    private List<MecanicoServicio> mecanicoServicios;
}
