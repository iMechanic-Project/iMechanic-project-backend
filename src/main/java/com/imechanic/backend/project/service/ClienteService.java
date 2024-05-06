package com.imechanic.backend.project.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.OrdenTrabajoClienteDTOList;
import com.imechanic.backend.project.controller.dto.OrderDetailDTO;
import com.imechanic.backend.project.controller.dto.PasoDTO;
import com.imechanic.backend.project.controller.dto.ServicioDetalleDTO;
import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.exception.RoleNotAuthorized;
import com.imechanic.backend.project.model.*;
import com.imechanic.backend.project.repository.CuentaRepository;
import com.imechanic.backend.project.repository.OrdenTrabajoRepository;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final CuentaRepository cuentaRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    @Transactional(readOnly = true)
    public List<OrdenTrabajoClienteDTOList> obtenerTodasLasOrdenesDeCliente(DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("CLIENTE")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'CLIENTE'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Cuenta cuenta = cuentaRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("Cuenta con correo: " + correoElectronico + " no encontrado"));

        List<OrdenTrabajo> ordenTrabajos = ordenTrabajoRepository.findAllByCorreoCliente(cuenta.getCorreoElectronico());

        return ordenTrabajos.stream()
                .map(orden -> new OrdenTrabajoClienteDTOList(
                        orden.getId(),
                        orden.getPlaca(),
                        orden.getCuenta().getNombre(),
                        dateFormat.format(orden.getFechaRegistro()), // Formatea la fecha
                        timeFormat.format(orden.getFechaRegistro()), // Formatea la hora
                        orden.getEstado().toString()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDetailDTO obtenerDetalleOrden(Long orderId, DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("CLIENTE")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'CLIENTE'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Cuenta cuenta = cuentaRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró el cliente con correo: " + correoElectronico));

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findByIdAndAndCorreoCliente(orderId, cuenta.getCorreoElectronico())
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró la orden de trabajo con ID: " + orderId + " para el cliente"));

        // Obtener la lista de servicios con detalles
        List<ServicioMecanico> serviciosMecanicos = ordenTrabajo.getServiciosMecanicos();
        List<ServicioDetalleDTO> serviciosDetalle = serviciosMecanicos.stream()
                .map(servicioMecanico -> {
                    Servicio servicio = servicioMecanico.getServicio();
                    Mecanico mecanico = servicioMecanico.getMecanico();
                    List<Paso> pasos = servicio.getPasos();
                    List<PasoDTO> pasosDTO = pasos.stream()
                            .map(paso -> new PasoDTO(paso.getNombre(), paso.isCompletado()))
                            .collect(Collectors.toList());
                    return new ServicioDetalleDTO(servicio.getNombre(), mecanico.getNombre(), servicio.getEstadoServicio().name(), pasosDTO);
                })
                .collect(Collectors.toList());

        return new OrderDetailDTO(ordenTrabajo.getCuenta().getNombre(),
                ordenTrabajo.getCuenta().getDireccion(),
                ordenTrabajo.getCuenta().getTelefono(),
                serviciosDetalle);
    }
}
