package com.imechanic.backend.project.repository;

import com.imechanic.backend.project.model.Mecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {
    Optional<Mecanico> findByCorreoElectronico(String correoElectronico);
    List<Mecanico> findMecanicosByCuentaCorreoElectronico(String correoElectronico);

    @Query("SELECT m FROM Mecanico m JOIN m.servicios s WHERE s.id = :serviceId")
    List<Mecanico> findAllByServiciosId(@Param("serviceId") Long serviceId);
}