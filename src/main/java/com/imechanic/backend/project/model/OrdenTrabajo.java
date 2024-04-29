package com.imechanic.backend.project.model;

import com.imechanic.backend.project.enumeration.EstadoOrden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

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

    @Column(name = "codigo_orden", unique = true)
    private String codigoOrden;
    private String placa;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    @ManyToOne(targetEntity = Cuenta.class)
    private Cuenta cuenta;

    @PrePersist
    protected void addOnAttributes() {
        fechaRegistro = new Date();
        UUID uuid = UUID.randomUUID();
        String codigoOrden = uuid.toString().toUpperCase().replaceAll("-", "").substring(0, 8);
        this.codigoOrden = codigoOrden.substring(0, 4) + "-" + codigoOrden.substring(4,8);
        this.estado = EstadoOrden.EN_ESPERA;
    }

}