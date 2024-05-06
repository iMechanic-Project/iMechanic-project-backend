package com.imechanic.backend.project.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.ServicioDTO;
import com.imechanic.backend.project.enumeration.TipoServicio;
import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.exception.RoleNotAuthorized;
import com.imechanic.backend.project.model.Cuenta;
import com.imechanic.backend.project.model.Servicio;
import com.imechanic.backend.project.repository.CuentaRepository;
import com.imechanic.backend.project.repository.ServicioRepository;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicioService {
    private final ServicioRepository servicioRepository;
    private final CuentaRepository cuentaRepository;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @Transactional(readOnly = true)
    public List<ServicioDTO> obtenerServiciosPorTipo(TipoServicio tipoServicio, DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }
        List<ServicioDTO> servicios = null;
        if (tipoServicio == TipoServicio.REPARACION) {
            servicios = servicioRepository.findAllReparaciones();
        } else if (tipoServicio == TipoServicio.MANTENIMIENTO) {
            servicios = servicioRepository.findAllMantenimientos();
        }
        if (servicios == null || servicios.isEmpty()) {
            throw new EntidadNoEncontrada("No se encontraron servicios para el tipo especificado.");
        }
        return servicios;
    }

    @Transactional(readOnly = true)
    public List<ServicioDTO> obtenerMisServicio(DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Cuenta cuenta = cuentaRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró la cuenta del taller con correo electrónico: " + correoElectronico));

        List<Servicio> serviciosActuales = cuenta.getServicios();

        // Mapear Servicio a ServicioDTO

        return serviciosActuales.stream()
                .map(servicio -> new ServicioDTO(servicio.getId(), servicio.getNombre()))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Servicio> agregarServiciosATaller(DecodedJWT decodedJWT, List<Long> serviciosIds) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Cuenta cuenta = cuentaRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró la cuenta del taller con correo electrónico: " + correoElectronico));

        List<Servicio> serviciosActuales = cuenta.getServicios();

        serviciosActuales.removeIf(servicio -> !serviciosIds.contains(servicio.getId()));

        List<Servicio> nuevosServicios = servicioRepository.findAllById(serviciosIds);
        for (Servicio nuevoServicio : nuevosServicios) {
            if (!serviciosActuales.contains(nuevoServicio)) {
                serviciosActuales.add(nuevoServicio);
            }
        }

        cuenta.setServicios(serviciosActuales);
        cuentaRepository.save(cuenta);

        return serviciosActuales;
    }

}