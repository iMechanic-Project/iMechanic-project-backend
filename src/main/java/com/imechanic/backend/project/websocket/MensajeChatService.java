package com.imechanic.backend.project.websocket;

import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.model.Cuenta;
import com.imechanic.backend.project.model.Mecanico;
import com.imechanic.backend.project.model.OrdenTrabajo;
import com.imechanic.backend.project.model.ServicioMecanico;
import com.imechanic.backend.project.repository.CuentaRepository;
import com.imechanic.backend.project.repository.OrdenTrabajoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MensajeChatService {
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final CuentaRepository cuentaRepository;
    private final MensajeChatRepository chatRepository;


    public void guardarMensaje(ChatMessage message, Long orderId, Long servicioMecanicoId) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(orderId)
                .orElseThrow(() -> new EntidadNoEncontrada("Orden de trabajo con ID: " + orderId + " no encontrada"));

        ServicioMecanico servicioMecanico = ordenTrabajo.getServiciosMecanicos().stream()
                .filter(servicio -> servicio.getId().equals(servicioMecanicoId))
                .findFirst()
                .orElseThrow(() -> new EntidadNoEncontrada("Servicio mecánico con ID: " + servicioMecanicoId + " no encontrado"));

        Cuenta remitente = cuentaRepository.findByCorreoElectronico(ordenTrabajo.getCorreoCliente())
                .orElseThrow(() -> new EntidadNoEncontrada("Cliente con correo: " + ordenTrabajo.getCorreoCliente() + " no encontrado"));

        Mecanico destinatario = servicioMecanico.getMecanico();

        MensajeChat chat = MensajeChat.builder()
                .remitente(remitente)
                .destinatario(destinatario)
                .contenido(message.getMessage())
                .build();
        chatRepository.save(chat);
    }
}
