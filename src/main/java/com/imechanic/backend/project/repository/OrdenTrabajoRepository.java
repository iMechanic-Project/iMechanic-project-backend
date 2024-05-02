package com.imechanic.backend.project.repository;

import com.imechanic.backend.project.model.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long> {
    List<OrdenTrabajo> findAllByCuentaCorreoElectronicoOrderByFechaRegistroDesc(String correoElectronico);
}
