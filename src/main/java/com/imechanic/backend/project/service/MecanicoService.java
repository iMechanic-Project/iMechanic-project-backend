package com.imechanic.backend.project.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.*;
import com.imechanic.backend.project.enumeration.EstadoOrden;
import com.imechanic.backend.project.enumeration.Role;
import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.exception.RoleNotAuthorized;
import com.imechanic.backend.project.model.*;
import com.imechanic.backend.project.repository.*;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MecanicoService {
    private final MecanicoRepository mecanicoRepository;
    private final PasswordEncoder passwordEncoder;
    private final CuentaRepository cuentaRepository;
    private final ServicioRepository servicioRepository;
    private final PasoRepository pasoRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final JavaMailSender javaMailSender;

    @Value("${spring.email.sender.user}")
    private String user;

    @Transactional(readOnly = true)
    public List<MecanicoDTO> getAllMechanicsByTaller(DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        String correoElectronico = decodedJWT.getSubject();

        List<Mecanico> mecanicos = mecanicoRepository.findMecanicosByCuentaCorreoElectronico(correoElectronico);

        return mecanicos.stream()
                .map(mecanico -> {
                    List<ServicioDTO> servicios = mecanico.getServicios().stream()
                            .map(servicio -> new ServicioDTO(servicio.getId(), servicio.getNombre()))
                            .collect(Collectors.toList());
                    return new MecanicoDTO(mecanico.getNombre(), mecanico.getCorreoElectronico(), servicios);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public MecanicoDTOResponse createMecanico(DecodedJWT decodedJWT, MecanicoDTORequest mecanicoDTORequest) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Cuenta cuenta = cuentaRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró la cuenta del taller con correo electrónico: " + correoElectronico));

        List<Servicio> serviciosActuales = cuenta.getServicios();

        // Obtener los servicios que se van a asignar al mecánico
        List<Servicio> serviciosSeleccionados = serviciosActuales.stream()
                .filter(servicio -> mecanicoDTORequest.getServicioIds().contains(servicio.getId()))
                .toList();

        Mecanico mecanico = Mecanico.builder()
                .nombre(mecanicoDTORequest.getNombre())
                .correoElectronico(mecanicoDTORequest.getCorreoElectronico())
                .contrasenia(passwordEncoder.encode(mecanicoDTORequest.getContrasenia()))
                .role(Role.MECANICO)
                .cuenta(cuenta)
                .isEnabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .servicios(serviciosSeleccionados)
                .build();

        mecanicoRepository.save(mecanico);

        Authentication authentication = new UsernamePasswordAuthenticationToken(mecanico.getCorreoElectronico(), mecanico.getContrasenia(), AuthorityUtils.createAuthorityList("ROLE_" + mecanico.getRole()));

        sendSimpleMessage(
                mecanico.getCorreoElectronico(),
                user,
                "Estimado/a mecanico " + mecanico.getNombre() + ",\n\nHa sido creada tu cuenta en iMechanic. La contraseña de tu cuenta es la siguiente: " + mecanicoDTORequest.getContrasenia() + " \n\nBienvenido,\nAl equipo de iMechanic");

        return new MecanicoDTOResponse("Mecanico '" + mecanicoDTORequest.getNombre() + "' creado con exito");
    }

    @Transactional(readOnly = true)
    public List<MecanicoDTOList> getAllMechanicsByService(DecodedJWT decodedJWT, Long serviceId) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        return mecanicoRepository.findAllByServiciosId(serviceId)
                .stream()
                .map(mecanico -> new MecanicoDTOList(mecanico.getId(), mecanico.getNombre()))
                .collect(Collectors.toList());
    }

    private void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(user);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Transactional(readOnly = true)
    public String iniciarServicioOrden(DecodedJWT decodedJWT, Long orderId) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("MECANICO") && !roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'MECANICO' ni 'TALLER'");
        }

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(orderId)
                .orElseThrow(() -> new EntidadNoEncontrada("Orden de trabajo con ID: " + orderId + " no encontrada"));

        ordenTrabajo.setEstado(EstadoOrden.EN_PROCESO);
        ordenTrabajoRepository.save(ordenTrabajo);

        return "Estado actualizado: " + ordenTrabajo.getEstado();
    }

    @Transactional
    public String completarPaso(Long serviceId, Long pasoId, DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("MECANICO")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'MECANICO'");
        }

        Servicio servicio = servicioRepository.findById(serviceId)
                .orElseThrow(() -> new EntidadNoEncontrada("Servicio con ID: " + serviceId + "no encontrado"));

        Paso paso = servicio.getPasos().stream()
                .filter(p -> p.getId().equals(pasoId))
                .findFirst()
                .orElseThrow(() -> new EntidadNoEncontrada("Paso con ID: " + pasoId + " no encontrado"));

        paso.setCompletado(true);
        pasoRepository.save(paso);

        return servicio.getPasos().toString();
    }

    @Transactional(readOnly = true)
    public OrderDetailMecanicoDTO obtenerDetalleOrden(Long orderId, DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("MECANICO")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'MECANICO'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Mecanico mecanico = mecanicoRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontro el mecanico con correo: " + correoElectronico));

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(orderId)
                .orElseThrow(() -> new EntidadNoEncontrada("Orden de trabajo con ID: " + orderId + " no encontrada"));

        ServicioMecanico servicioMecanico = ordenTrabajo.getServiciosMecanicos().stream()
                .filter(serv -> serv.getMecanico().equals(mecanico))
                .findFirst()
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró el servicio asignado al mecanico"));

        Servicio servicio = servicioMecanico.getServicio();

        List<Paso> pasos = servicio.getPasos();

        List<String> nombrePasos = pasos.stream()
                .map(Paso::getNombre)
                .toList();

        return new OrderDetailMecanicoDTO(ordenTrabajo.getNombreCliente(),
                ordenTrabajo.getDireccionCliente(),
                ordenTrabajo.getTelefonoCliente(),
                servicio.getNombre(),
                servicio.getEstadoServicio().toString(),
                mecanico.getNombre(),
                nombrePasos);
    }

    @Transactional(readOnly = true)
    public String terminarServicioOrden(DecodedJWT decodedJWT, Long orderId) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("MECANICO") && !roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'MECANICO' ni 'TALLER'");
        }

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(orderId)
                .orElseThrow(() -> new EntidadNoEncontrada("Orden de trabajo con ID: " + orderId + " no encontrada"));

        ordenTrabajo.setEstado(EstadoOrden.FINALIZADO);
        ordenTrabajoRepository.save(ordenTrabajo);

        return "Estado actualizado: " + ordenTrabajo.getEstado();
    }
}