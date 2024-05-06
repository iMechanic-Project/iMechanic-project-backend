package com.imechanic.backend.project.model;

import com.imechanic.backend.project.enumeration.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "cuenta")
@Inheritance(strategy = InheritanceType.JOINED)
@Builder
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo 'correo' es obligatorio")
    @Email(message = "El campo 'correo' debe tener un formato válido")
    @Column(name = "correo_electronico", unique = true)
    private String correoElectronico;

    @NotBlank(message = "El campo 'contraseña' es obligatorio")
    @Size(min = 8, message = "El campos 'contraseña' debe tener como minimo 8 caracteres")
    private String contrasenia;

    @NotBlank(message = "El campo 'nombre' es obligatorio")
    private String nombre;

    @NotBlank(message = "El campo 'telefono' es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe contener solo 9 números")
    private String telefono;

    @NotBlank(message = "El campo 'direccion' es obligatorio")
    private String direccion;

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

    @ManyToMany(targetEntity = Servicio.class, fetch = FetchType.LAZY)
    private List<Servicio> servicios;

    @OneToMany(targetEntity = Mecanico.class, fetch = FetchType.LAZY, mappedBy = "cuenta")
    private List<Mecanico> mecanicos;

    @OneToMany(targetEntity = Vehiculo.class, fetch = FetchType.LAZY, mappedBy = "cuenta")
    private List<Vehiculo> vehiculos;

    @OneToMany(targetEntity = OrdenTrabajo.class, fetch = FetchType.LAZY, mappedBy = "cuenta")
    private List<OrdenTrabajo> ordenTrabajos;
}
