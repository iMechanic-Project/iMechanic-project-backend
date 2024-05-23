package com.imechanic.backend.project.repository;

import com.imechanic.backend.project.model.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long> {
    List<OrdenTrabajo> findAllByCuentaCorreoElectronicoOrderByFechaRegistroDesc(String correoElectronico);
    List<OrdenTrabajo> findAllByCorreoCliente(String correoElectronico);
    Optional<OrdenTrabajo> findByIdAndAndCorreoCliente(Long orderId, String correoElectronico);
    Optional<OrdenTrabajo> findByIdAndAndCuentaId(Long orderId, Long cuentaId);

    @Query("SELECT ot FROM OrdenTrabajo ot JOIN ot.serviciosMecanicos sm WHERE sm.mecanico.id = :mecanicoId")
    List<OrdenTrabajo> findAllByMecanicoId(@Param("mecanicoId") Long mecanicoId);
}
