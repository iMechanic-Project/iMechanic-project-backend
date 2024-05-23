package com.imechanic.backend.project.model;

import com.imechanic.backend.project.enumeration.EstadoOrden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orden_trabajo")
@Builder
public class OrdenTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "correo_cliente")
    private String correoCliente;

    @Column(name = "nombre_cliente")
    private String nombreCliente;

    @Column(name = "direccion_cliente")
    private String direccionCliente;

    @Column(name = "telefono_cliente")
    private String telefonoCliente;

    private String placa;

    private String marca;

    private String modelo;

    private String categoria;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    @ManyToOne(targetEntity = Cuenta.class)
    private Cuenta cuenta;

    @OneToMany(mappedBy = "ordenTrabajo", cascade = CascadeType.ALL)
    private List<ServicioMecanico> serviciosMecanicos;

    @PrePersist
    protected void addOnAttributes() {
        fechaRegistro = new Date();
    }
}
