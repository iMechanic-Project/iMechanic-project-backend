package com.imechanic.backend.project.repository;

import com.imechanic.backend.project.model.Mecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {
    Optional<Mecanico> findByCorreoElectronico(String correoElectronico);
}
