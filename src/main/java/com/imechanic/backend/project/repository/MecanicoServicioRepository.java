package com.imechanic.backend.project.repository;

import com.imechanic.backend.project.model.MecanicoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MecanicoServicioRepository extends JpaRepository<MecanicoServicio, Long> {
}
