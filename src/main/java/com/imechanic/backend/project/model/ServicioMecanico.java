package com.imechanic.backend.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "servicio_mecanico")
public class ServicioMecanico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "servicio_id", referencedColumnName = "id")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "mecanico_id", referencedColumnName = "id")
    private Mecanico mecanico;

    @ManyToOne
    @JoinColumn(name = "orden_trabajo_id", referencedColumnName = "id")
    private OrdenTrabajo ordenTrabajo;

    public ServicioMecanico(Servicio servicioId, Mecanico mecanicoId) {
        this.servicio = servicioId;
        this.mecanico = mecanicoId;
    }
}
