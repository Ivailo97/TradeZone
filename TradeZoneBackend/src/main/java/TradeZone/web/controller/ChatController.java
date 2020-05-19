package TradeZone.web.controller;

import TradeZone.data.model.rest.chat.ChatRestModel;
import TradeZone.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * Sends a message to its destination channel
     *
     * @param message
     */
    @MessageMapping("/messages")
    public void handleMessage(ChatRestModel message) {
        chatService.saveAndSend(message);
    }
}
