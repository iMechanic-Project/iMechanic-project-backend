package com.imechanic.backend.project.websocket;

import com.imechanic.backend.project.model.OrdenTrabajo;
import com.imechanic.backend.project.model.ServicioMecanico;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "sala_chat")
public class SalaChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = OrdenTrabajo.class)
    private OrdenTrabajo ordenTrabajo;

    @ManyToOne(targetEntity = ServicioMecanico.class)
    private ServicioMecanico servicioMecanico;

    @OneToMany(targetEntity = MensajeChat.class, cascade = CascadeType.ALL, mappedBy = "sala")
    private List<MensajeChat> mensajes;
}
