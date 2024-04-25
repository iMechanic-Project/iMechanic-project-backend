package com.imechanic.backend.project.service;

import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.model.Cuenta;
import com.imechanic.backend.project.model.Servicio;
import com.imechanic.backend.project.repository.CuentaRepository;
import com.imechanic.backend.project.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioService {
    private final ServicioRepository servicioRepository;
    private final CuentaRepository cuentaRepository;

    @Transactional
    public void agregarServiciosATaller(Long tallerId, List<Long> serviciosIds) {
        Optional<Cuenta> cuentaOptional = cuentaRepository.findById(tallerId);
        if (cuentaOptional.isEmpty()) {
            throw new EntidadNoEncontrada("No se encontró la cuenta del taller con ID: " + tallerId);
        }

        Cuenta cuenta = cuentaOptional.get();
        List<Servicio> serviciosActuales = cuenta.getServicios();

        serviciosActuales.removeIf(servicio -> !serviciosIds.contains(servicio.getId()));

        List<Servicio> nuevosServicios = servicioRepository.findAllById(serviciosIds);
        for (Servicio nuevoServicio : nuevosServicios) {
            if (!serviciosActuales.contains(nuevoServicio)) {
                serviciosActuales.add(nuevoServicio);
            }
        }

        cuentaRepository.save(cuenta);
    }
}