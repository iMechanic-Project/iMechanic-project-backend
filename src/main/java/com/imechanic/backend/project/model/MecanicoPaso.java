package com.imechanic.backend.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mecanico_paso")
@Builder
public class MecanicoPaso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orden_trabajo_id", referencedColumnName = "id")
    private OrdenTrabajo ordenTrabajo;

    @ManyToOne
    @JoinColumn(name = "mecanico_id", referencedColumnName = "id")
    private Mecanico mecanico;

    @ManyToOne
    @JoinColumn(name = "servicio_id", referencedColumnName = "id")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "paso_id", referencedColumnName = "id")
    private Paso paso;

    private boolean complete;
}
