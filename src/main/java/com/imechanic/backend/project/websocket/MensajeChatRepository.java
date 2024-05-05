package com.imechanic.backend.project.websocket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeChatRepository extends JpaRepository<MensajeChat, Long> {
}
