package com.imechanic.backend.project.repository;

import com.imechanic.backend.project.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findCuentaByCorreoElectronico(String correoElectronico);
}
