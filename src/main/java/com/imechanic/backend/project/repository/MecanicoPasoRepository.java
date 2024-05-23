package com.imechanic.backend.project.repository;

import com.imechanic.backend.project.model.MecanicoPaso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MecanicoPasoRepository extends JpaRepository<MecanicoPaso, Long> {
    boolean existsByOrdenTrabajoIdAndServicioIdAndPasoId(Long ordenTrabajoId, Long servicioId, Long pasoId);

    List<MecanicoPaso> findAllByMecanicoIdAndServicioIdAndOrdenTrabajoId(Long mecanicoId, Long servicioId, Long ordenTrabajoId);
}
