package com.imechanic.backend.project.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.*;
import com.imechanic.backend.project.enumeration.Role;
import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.exception.RoleNotAuthorized;
import com.imechanic.backend.project.model.Cuenta;
import com.imechanic.backend.project.model.Mecanico;
import com.imechanic.backend.project.model.Servicio;
import com.imechanic.backend.project.repository.CuentaRepository;
import com.imechanic.backend.project.repository.MecanicoRepository;
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
}
