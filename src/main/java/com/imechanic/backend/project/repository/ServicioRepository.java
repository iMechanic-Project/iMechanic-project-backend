package com.imechanic.backend.project.repository;

import com.imechanic.backend.project.controller.dto.ServicioDTO;
import com.imechanic.backend.project.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    @Query("SELECT new com.imechanic.backend.project.controller.dto.ServicioDTO(s.id, s.nombre) FROM Servicio s WHERE s.tipoServicio = com.imechanic.backend.project.enumeration.TipoServicio.REPARACION")
    List<ServicioDTO> findAllReparaciones();

    @Query("SELECT new com.imechanic.backend.project.controller.dto.ServicioDTO(s.id, s.nombre) FROM Servicio s WHERE s.tipoServicio = com.imechanic.backend.project.enumeration.TipoServicio.MANTENIMIENTO")
    List<ServicioDTO> findAllMantenimientos();
}