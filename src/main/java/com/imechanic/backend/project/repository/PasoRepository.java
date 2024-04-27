package com.imechanic.backend.project.repository;

import com.imechanic.backend.project.model.Paso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasoRepository extends JpaRepository<Paso, Long> {
}
