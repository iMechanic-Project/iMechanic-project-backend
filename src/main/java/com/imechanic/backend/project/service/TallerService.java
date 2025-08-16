package com.imechanic.backend.project.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.*;
import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.exception.RoleNotAuthorized;
import com.imechanic.backend.project.model.*;
import com.imechanic.backend.project.repository.CuentaRepository;
import com.imechanic.backend.project.repository.OrdenTrabajoRepository;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TallerService {
    private final CuentaRepository cuentaRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @Transactional(readOnly = true)
    public OrderDetailDTO obtenerDetalleOrden(Long orderId, DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Cuenta cuenta = cuentaRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró el cliente con correo: " + correoElectronico));

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findByIdAndAndCuentaId(orderId, cuenta.getId())
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró la orden de trabajo con ID: " + orderId + " para el cliente"));

        // Obtener la lista de servicios con detalles
        List<ServicioMecanico> serviciosMecanicos = ordenTrabajo.getServiciosMecanicos();
        List<ServicioDetalleDTO> serviciosDetalle = serviciosMecanicos.stream()
                .map(servicioMecanico -> {
                    Servicio servicio = servicioMecanico.getServicio();
                    Mecanico mecanico = servicioMecanico.getMecanico();
                    List<Paso> pasos = servicio.getPasos();
                    List<PasoDTO> pasosDTO = pasos.stream()
                            .map(paso -> new PasoDTO(paso.getId(), paso.getNombre()))
                            .collect(Collectors.toList());
                    return new ServicioDetalleDTO(new ServicioDTO(servicio.getId(), servicio.getNombre()), new MecanicoDTOList(mecanico.getId(), mecanico.getNombre()), servicio.getEstadoServicio().name(), pasosDTO);
                })
                .collect(Collectors.toList());

        return new OrderDetailDTO(ordenTrabajo.getId(),
                ordenTrabajo.getCuenta().getNombre(),
                ordenTrabajo.getCuenta().getDireccion(),
                ordenTrabajo.getCuenta().getTelefono(),
                serviciosDetalle);
    }

    @Transactional(readOnly = true)
    public List<Paso> getPasosCompletados(DecodedJWT decodedJWT, Long ordenId) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        Cuenta cuenta = cuentaRepository.findByCorreoElectronico(decodedJWT.getSubject())
                .orElseThrow(() -> new EntidadNoEncontrada("Mecanico con correo: " + decodedJWT.getSubject() + " no encontrado"));

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(ordenId)
                .orElseThrow(() -> new EntidadNoEncontrada("Orden de trabajo con ID: " + ordenId + " no encontrada"));

        // Verificar si el mecánico está asignado a la orden de trabajo y al servicio
        ServicioMecanico servicioMecanico = ordenTrabajo.getServiciosMecanicos().stream()
                .filter(servicio -> servicio.getMecanico().equals(mecanico) && servicio.getServicio().getId().equals(servicioId))
                .findFirst()
                .orElseThrow(() -> new EntidadNoEncontrada("El mecánico no está asignado al servicio en la orden de trabajo"));

        // Obtener los MecanicoPaso completados del mecánico en el servicio específico
        List<MecanicoPaso> mecanicoPasosCompletados = mecanicoPasoRepository.findAllByMecanicoIdAndServicioIdAndOrdenTrabajoId(mecanico.getId(), servicioMecanico.getServicio().getId(), ordenTrabajo.getId());

        // Extraer los pasos completados de los registros de MecanicoPaso

        return mecanicoPasosCompletados.stream()
                .filter(MecanicoPaso::isComplete)
                .map(MecanicoPaso::getPaso)
                .collect(Collectors.toList());
    }
}
