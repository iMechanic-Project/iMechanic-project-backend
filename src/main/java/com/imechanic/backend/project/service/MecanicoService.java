package com.imechanic.backend.project.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.*;
import com.imechanic.backend.project.enumeration.EstadoOrden;
import com.imechanic.backend.project.enumeration.Role;
import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.exception.PasoYaCompletado;
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

import java.text.SimpleDateFormat;
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
    private final MecanicoServicioRepository mecanicoServicioRepository;
    private final MecanicoPasoRepository mecanicoPasoRepository;
    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final JavaMailSender javaMailSender;

    @Value("${spring.email.sender.user}")
    private String user;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

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
                    List<ServicioDTO> servicios = mecanico.getMecanicoServicios().stream()
                            .map(mecanicoServicio -> new ServicioDTO(mecanicoServicio.getServicio().getId(), mecanicoServicio.getServicio().getNombre()))
                            .collect(Collectors.toList());
                    return new MecanicoDTO(mecanico.getNombre(), mecanico.getCorreoElectronico(), servicios);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MecanicoDTOList> getAllMechanicsByTallerForOrder(DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        String correoElectronico = decodedJWT.getSubject();

        List<Mecanico> mecanicos = mecanicoRepository.findMecanicosByCuentaCorreoElectronico(correoElectronico);

        return mecanicos.stream()
                .map(mecanico -> {
                    return new MecanicoDTOList(mecanico.getId(), mecanico.getNombre());
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
        List<Servicio> serviciosSeleccionados = mecanicoDTORequest.getServicioIds().stream()
                .map(servicioId -> servicioRepository.findById(servicioId)
                        .orElseThrow(() -> new EntidadNoEncontrada("No se encontró el servicio con ID: " + servicioId)))
                .collect(Collectors.toList());

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
                .build();

        mecanicoRepository.save(mecanico);

        mecanicoRepository.save(mecanico);

        // Asignar los servicios al mecánico
        for (Servicio servicio : serviciosSeleccionados) {
            MecanicoServicio mecanicoServicio = new MecanicoServicio();
            mecanicoServicio.setMecanico(mecanico);
            mecanicoServicio.setServicio(servicio);
            mecanicoServicioRepository.save(mecanicoServicio);
        }

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
    public List<OrdenTrabajoMecanicoDTOList> obtenerTodasLasOrdenesDeMecanico(DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("MECANICO")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'MECANICO'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Mecanico mecanico = mecanicoRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("Mecanico con correo: " + correoElectronico + " no encontrado"));

        List<OrdenTrabajo> ordenTrabajos = ordenTrabajoRepository.findAllByMecanicoId(mecanico.getId());

        return ordenTrabajos.stream()
                .map(orden -> new OrdenTrabajoMecanicoDTOList(
                        orden.getId(),
                        orden.getPlaca(),
                        dateFormat.format(orden.getFechaRegistro()), // Formatea la fecha
                        timeFormat.format(orden.getFechaRegistro()), // Formatea la hora
                        orden.getEstado().toString()
                ))
                .collect(Collectors.toList());
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

        List<PasoDTO> nombrePasos = servicio.getPasos().stream()
                .map(paso -> new PasoDTO(paso.getId(), paso.getNombre()))
                .toList();

        return new OrderDetailMecanicoDTO(ordenTrabajo.getId(),
                ordenTrabajo.getNombreCliente(),
                ordenTrabajo.getDireccionCliente(),
                ordenTrabajo.getTelefonoCliente(),
                new ServicioDTO(servicio.getId(), servicio.getNombre()),
                servicio.getEstadoServicio().toString(),
                new MecanicoDTOList(mecanico.getId(), mecanico.getNombre()),
                nombrePasos);
    }

    @Transactional(readOnly = true)
    public String iniciarServicioOrden(DecodedJWT decodedJWT, Long orderId) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("MECANICO") && !roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'MECANICO' ni 'TALLER'");
        }

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(orderId)
                .orElseThrow(() -> new EntidadNoEncontrada("Orden de trabajo con ID: " + orderId + " no encontrada"));

        if (ordenTrabajo.getEstado() == EstadoOrden.EN_PROCESO) {
            return "ESTADO DE ORDEN ACTUALIZADA 1: " + ordenTrabajo.getEstado();
        }

        if (ordenTrabajo.getEstado() == EstadoOrden.FINALIZADO) {
            return "ESTADO DE ORDEN ACTUALIZADA 2: "+ ordenTrabajo.getEstado();
        }

        ordenTrabajo.setEstado(EstadoOrden.EN_PROCESO);
        ordenTrabajoRepository.save(ordenTrabajo);
        return "ESTADO DE ORDEN ACTUALIZADA 3: " + ordenTrabajo.getEstado();
    }

    @Transactional
    public MecanicoPasoDTO completarPaso(DecodedJWT decodedJWT, Long ordenId, Long servicioId, Long pasoId) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("MECANICO")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'MECANICO'");
        }

        Mecanico mecanico = mecanicoRepository.findByCorreoElectronico(decodedJWT.getSubject())
                .orElseThrow(() -> new EntidadNoEncontrada("Mecanico con correo: " + decodedJWT.getSubject() + " no encontrado"));

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(ordenId)
                .orElseThrow(() -> new EntidadNoEncontrada("Orden de trabajo con ID: " + ordenId + " no encontrada"));

        // Verificar si el mecánico está asignado a la orden de trabajo y al servicio
        ServicioMecanico servicioMecanico = ordenTrabajo.getServiciosMecanicos().stream()
                .filter(servicio -> servicio.getMecanico().equals(mecanico) && servicio.getServicio().getId().equals(servicioId))
                .findFirst()
                .orElseThrow(() -> new EntidadNoEncontrada("El mecánico no está asignado al servicio en la orden de trabajo"));

        // Verificar si el paso ya ha sido completado
        boolean pasoCompletado = mecanicoPasoRepository.existsByOrdenTrabajoIdAndServicioIdAndPasoId(ordenId, servicioId, pasoId);
        if (pasoCompletado) {
            throw new PasoYaCompletado("El paso con ID: " + pasoId + " ya ha sido completado para esta orden de trabajo y servicio");
        }

        // Buscar el paso
        Paso paso = pasoRepository.findById(pasoId)
                .orElseThrow(() -> new EntidadNoEncontrada("Paso con ID: " + pasoId + " no encontrado"));

        // Completar el paso
        MecanicoPaso mecanicoPaso = MecanicoPaso.builder()
                .ordenTrabajo(ordenTrabajo)
                .mecanico(mecanico)
                .servicio(servicioMecanico.getServicio())
                .paso(paso)
                .complete(true)
                .build();

        mecanicoPasoRepository.save(mecanicoPaso);

        return new MecanicoPasoDTO(ordenTrabajo.getId(), mecanico.getId(), servicioMecanico.getServicio().getId(), servicioMecanico.getServicio().getNombre(), paso.getId(), mecanicoPaso.isComplete());
    }

    @Transactional(readOnly = true)
    public List<Paso> getPasosCompletados(DecodedJWT decodedJWT, Long ordenId, Long servicioId) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("MECANICO")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'MECANICO'");
        }

        Mecanico mecanico = mecanicoRepository.findByCorreoElectronico(decodedJWT.getSubject())
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