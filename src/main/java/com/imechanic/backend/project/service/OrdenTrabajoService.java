package com.imechanic.backend.project.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.OrdenTrabajoDTOList;
import com.imechanic.backend.project.controller.dto.OrdenTrabajoDTORequest;
import com.imechanic.backend.project.controller.dto.OrdenTrabajoDTOResponse;
import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.exception.RoleNotAuthorized;
import com.imechanic.backend.project.model.Cuenta;
import com.imechanic.backend.project.model.OrdenTrabajo;
import com.imechanic.backend.project.model.Vehiculo;
import com.imechanic.backend.project.repository.CuentaRepository;
import com.imechanic.backend.project.repository.OrdenTrabajoRepository;
import com.imechanic.backend.project.repository.VehiculoRepository;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenTrabajoService {
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final CuentaRepository cuentaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public OrdenTrabajoDTOResponse crearOrden(DecodedJWT decodedJWT, OrdenTrabajoDTORequest ordenTrabajoDTORequest) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Cuenta cuentaTaller = cuentaRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró la cuenta del cliente con correo electronico " + correoElectronico));

        Vehiculo vehiculo = vehiculoRepository.findByPlaca(ordenTrabajoDTORequest.getPlaca())
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró el vehiculo con placa: " + ordenTrabajoDTORequest.getPlaca()));

        OrdenTrabajo ordenTrabajo = OrdenTrabajo.builder()
                .placa(ordenTrabajoDTORequest.getPlaca())
                .cuenta(cuentaTaller)
                .nombreCliente(vehiculo.getCuenta().getNombre())
                .build();

        ordenTrabajoRepository.save(ordenTrabajo);

        return new OrdenTrabajoDTOResponse(cuentaTaller.getNombre(), cuentaTaller.getDireccion(), cuentaTaller.getTelefono(), vehiculo.getPlaca(), vehiculo.getMarca().getNombre(), vehiculo.getModelo().getNombre(), vehiculo.getCategoria().toString());
    }

    public List<OrdenTrabajoDTOList> obtenerTodasLasOrdenesDeTaller(DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        String correoElectronico = decodedJWT.getSubject();

        List<OrdenTrabajo> ordenTrabajos = ordenTrabajoRepository.findAllByCuentaCorreoElectronico(correoElectronico);

        return ordenTrabajos.stream()
                .map(orden -> new OrdenTrabajoDTOList(
                        orden.getPlaca(),
                        orden.getNombreCliente(),
                        dateFormat.format(orden.getFechaRegistro()), // Formatea la fecha
                        timeFormat.format(orden.getFechaRegistro()), // Formatea la hora
                        orden.getEstado().toString()
                ))
                .collect(Collectors.toList());
    }
}
