package com.imechanic.backend.project.websocket;

import com.imechanic.backend.project.model.Cuenta;
import com.imechanic.backend.project.model.Mecanico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mensaje_chat")
@Builder
public class MensajeChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Verificar funcionamiento correcto remitente - destinatario
     */
    @ManyToOne(targetEntity = Cuenta.class)
    private Cuenta remitente;

    /**
     * Verficar funcionamiento correcto destinatario - remitente
     */
    @ManyToOne(targetEntity = Mecanico.class)
    private Mecanico destinatario;

    private String contenido;

    private Date fechaEnvio;


    @ManyToOne(targetEntity = SalaChat.class)
    private SalaChat sala;

    @PrePersist
    protected void addOnAttributes() {
        this.fechaEnvio = new Date();
    }
}