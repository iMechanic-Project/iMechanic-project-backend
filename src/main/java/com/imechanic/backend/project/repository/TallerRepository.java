package com.imechanic.backend.project.repository;

import com.imechanic.backend.project.model.Taller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TallerRepository extends JpaRepository<Taller, Long> {
}
