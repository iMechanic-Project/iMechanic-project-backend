package com.imechanic.backend.project.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.*;
import com.imechanic.backend.project.enumeration.EstadoOrden;
import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.exception.RoleNotAuthorized;
import com.imechanic.backend.project.model.*;
import com.imechanic.backend.project.repository.*;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenTrabajoService {
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final CuentaRepository cuentaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ServicioRepository servicioRepository;
    private final MecanicoRepository mecanicoRepository;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public OrdenTrabajoDTOResponse buscarPorPlaca(DecodedJWT decodedJWT, OrdenTrabajoDTORequest ordenTrabajoDTORequest) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Cuenta cuentaTaller = cuentaRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró la cuenta del cliente con correo electronico " + correoElectronico));

        Vehiculo vehiculo = vehiculoRepository.findByPlaca(ordenTrabajoDTORequest.getPlaca())
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró el vehiculo con placa: " + ordenTrabajoDTORequest.getPlaca()));

        return new OrdenTrabajoDTOResponse(cuentaTaller.getCorreoElectronico(), vehiculo.getCuenta().getNombre(), vehiculo.getCuenta().getDireccion(), vehiculo.getCuenta().getTelefono(), vehiculo.getPlaca(), vehiculo.getMarca().getNombre(), vehiculo.getModelo().getNombre(), vehiculo.getCategoria().toString());
    }

    public OrdenTrabajoDTOResponse crearOrden(DecodedJWT decodedJWT, CreateOrdenDTORequest createOrdenDTORequest) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Cuenta cuentaTaller = cuentaRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró la cuenta del cliente con correo electronico " + correoElectronico));

        OrdenTrabajo ordenTrabajo = OrdenTrabajo.builder()
                .nombreCliente(createOrdenDTORequest.getNombreCliente())
                .direccionCliente(createOrdenDTORequest.getDireccion())
                .telefonoCliente(createOrdenDTORequest.getTelefono())
                .placa(createOrdenDTORequest.getPlaca())
                .marca(createOrdenDTORequest.getMarca())
                .modelo(createOrdenDTORequest.getModelo())
                .categoria(createOrdenDTORequest.getCategoria())
                .estado(EstadoOrden.EN_ESPERA)
                .cuenta(cuentaTaller)
                .serviciosMecanicos(new ArrayList<>())
                .build();

        ordenTrabajoRepository.save(ordenTrabajo);

        for (ServicioMecanicoDTO servicioMecanicoDTO : createOrdenDTORequest.getServiciosMecanicos()) {
            Servicio servicio = servicioRepository.findById(servicioMecanicoDTO.getServicioId())
                    .orElseThrow(() -> new EntidadNoEncontrada("No se encontró el servicio con id: " + servicioMecanicoDTO.getServicioId()));
            Mecanico mecanico = mecanicoRepository.findById(servicioMecanicoDTO.getMecanicoId())
                    .orElseThrow(() -> new EntidadNoEncontrada("No se encontró el mecánico con id: " + servicioMecanicoDTO.getMecanicoId()));

            ServicioMecanico servicioMecanico = new ServicioMecanico(servicio, mecanico);
            servicioMecanico.setOrdenTrabajo(ordenTrabajo);
            ordenTrabajo.getServiciosMecanicos().add(servicioMecanico);
        }

        ordenTrabajoRepository.save(ordenTrabajo);

        return new OrdenTrabajoDTOResponse(cuentaTaller.getCorreoElectronico(),
                ordenTrabajo.getNombreCliente(),
                ordenTrabajo.getDireccionCliente(),
                ordenTrabajo.getTelefonoCliente(),
                ordenTrabajo.getPlaca(),
                ordenTrabajo.getMarca(),
                ordenTrabajo.getModelo(),
                ordenTrabajo.getCategoria());
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
