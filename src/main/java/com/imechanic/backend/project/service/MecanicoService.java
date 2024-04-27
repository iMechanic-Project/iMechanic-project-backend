package com.imechanic.backend.project.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.MecanicoDTORequest;
import com.imechanic.backend.project.controller.dto.MecanicoDTOResponse;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MecanicoService {
    private final MecanicoRepository mecanicoRepository;
    private final PasswordEncoder passwordEncoder;
    private final CuentaRepository cuentaRepository;
    private final JwtAuthenticationManager jwtAuthenticationManager;

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
        return new MecanicoDTOResponse("Mecanico '" + mecanicoDTORequest.getNombre() + "' creado con exito");
    }
}
