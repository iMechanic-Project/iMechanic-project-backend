package com.imechanic.backend.project.model;

import com.imechanic.backend.project.enumeration.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mecanico")
@Builder
public class Mecanico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo 'nombre' es obligatorio")
    private String nombre;

    @NotBlank(message = "El campo 'correo' es obligatorio")
    @Email(message = "El campo 'correo' debe tener un formato válido")
    @Column(name = "correo_electronico", unique = true)
    private String correoElectronico;

    @NotBlank(message = "El campo 'contraseña' es obligatorio")
    @Size(min = 8, message = "El campos 'contraseña' debe tener como minimo 8 caracteres")
    private String contrasenia;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "account_no_expired")
    private boolean accountNoExpired;

    @Column(name = "account_no_locked")
    private boolean accountNoLocked;

    @Column(name = "credential_no_expired")
    private boolean credentialNoExpired;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(targetEntity = Cuenta.class)
    private Cuenta cuenta;

    @OneToMany(mappedBy = "mecanico")
    private List<MecanicoServicio> mecanicoServicios;

    @OneToMany(mappedBy = "mecanico")
    private List<MecanicoPaso> mecanicoPasos;
}
