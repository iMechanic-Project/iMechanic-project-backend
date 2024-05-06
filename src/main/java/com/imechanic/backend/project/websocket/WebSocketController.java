package com.imechanic.backend.project.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final MensajeChatService chatService;
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage chat(@DestinationVariable String roomId, ChatMessage message, Long orderId, Long servicioMecanicoId) {
        chatService.guardarMensaje(message, orderId, servicioMecanicoId);
        System.out.println(message);
        return new ChatMessage(message.getMessage(), message.getUser());
    }
}
