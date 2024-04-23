package com.imechanic.backend.project.security.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.AuthenticationLoginDTORequest;
import com.imechanic.backend.project.controller.dto.AuthenticationSignUpDTORequest;
import com.imechanic.backend.project.controller.dto.LoginDTOResponse;
import com.imechanic.backend.project.controller.dto.SignUpDTOResponse;
import com.imechanic.backend.project.enumeration.Role;
import com.imechanic.backend.project.exception.CredencialesIncorrectas;
import com.imechanic.backend.project.exception.TokenNotFound;
import com.imechanic.backend.project.model.Cuenta;
import com.imechanic.backend.project.repository.CuentaRepository;
import com.imechanic.backend.project.security.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IUserDetailService implements UserDetailsService {
    private final CuentaRepository cuentaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final JavaMailSender javaMailSender;

    @Value("${spring.email.sender.user}")
    private String user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cuenta cuenta = cuentaRepository.findCuentaByCorreoElectronico(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario" + username + " no existe"));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_".concat(cuenta.getRole().name()));

        return new User(
                cuenta.getCorreoElectronico(),
                cuenta.getContrasenia(),
                cuenta.isEnabled(),
                cuenta.isAccountNoExpired(),
                cuenta.isCredentialNoExpired(),
                cuenta.isAccountNoLocked(),
                Collections.singleton(authority));
    }

    public LoginDTOResponse loginUser(AuthenticationLoginDTORequest loginDTORequest) {
        String correoElectronico = loginDTORequest.correoElectronico();
        String contrasenia = loginDTORequest.contrasenia();

        Authentication authentication = authenticate(correoElectronico, contrasenia);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = loadUserByUsername(correoElectronico);
        if (!userDetails.isEnabled()) {
            throw new CredencialesIncorrectas("La cuenta no está habilitada");
        }

        String accessToken = jwtUtils.createToken(authentication);

        return new LoginDTOResponse(correoElectronico, "User logged successfully", accessToken, true);
    }

    private Authentication authenticate(String correoElectronico, String contrasenia) {
        UserDetails userDetails = loadUserByUsername(correoElectronico);

        if (userDetails == null || !passwordEncoder.matches(contrasenia, userDetails.getPassword())) {
            throw new CredencialesIncorrectas("Invalid email or password");
        }

        return new UsernamePasswordAuthenticationToken(correoElectronico, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public SignUpDTOResponse createUser(AuthenticationSignUpDTORequest signUpDTORequest) {
        String correoElectronico = signUpDTORequest.correoElectronico();
        String contrasenia = signUpDTORequest.contrasenia();
        String nombre = signUpDTORequest.nombre();
        String telefono = signUpDTORequest.telefono();
        String direccion = signUpDTORequest.direccion();
        Role role = Role.valueOf(signUpDTORequest.role().toUpperCase());

        Cuenta cuenta = Cuenta.builder()
                .correoElectronico(correoElectronico)
                .contrasenia(passwordEncoder.encode(contrasenia))
                .nombre(nombre)
                .telefono(telefono)
                .direccion(direccion)
                .role(role)
                .isEnabled(false)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();

        Cuenta cuentaCreated = cuentaRepository.save(cuenta);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_".concat(cuenta.getRole().name()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(cuentaCreated.getCorreoElectronico(), cuentaCreated.getContrasenia(), Collections.singleton(authority));
        String accessToken = jwtUtils.createToken(authentication);

        sendSimpleMessage(
                cuentaCreated.getCorreoElectronico(),
                user,
                "Estimado/a " + cuentaCreated.getNombre() + ",\n\nGracias por registrarte en iMechanic. Por favor, haz clic en el siguiente enlace para confirmar tu cuenta:\n\nhttp://localhost:4200/verificar/" + accessToken + "/" + cuentaCreated.getNombre() + "\n\nSaludos,\nEl equipo de iMechanic");

        return new SignUpDTOResponse("Welcome to iMechanic '".concat(correoElectronico).concat("'"));
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public String confirmarCuenta(String token) {
        DecodedJWT decodedJWT = jwtUtils.validateToken(token);
        String username = jwtUtils.extractUsername(decodedJWT);

        // Buscar la cuenta asociada al token
        Optional<Cuenta> cuentaOptional = cuentaRepository.findCuentaByCorreoElectronico(username);

        if (cuentaOptional.isEmpty()) {
            throw new TokenNotFound("Token no válido");
        }

        // Actualizar el estado de la cuenta para habilitarla
        Cuenta cuenta = cuentaOptional.get();
        cuenta.setEnabled(true);
        cuentaRepository.save(cuenta);

        return "Cuenta confirmada exitosamente";
    }

}
