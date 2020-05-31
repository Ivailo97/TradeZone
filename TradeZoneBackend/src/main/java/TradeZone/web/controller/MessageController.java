package TradeZone.web.controller;

import TradeZone.data.model.entity.ChatMessage;
import TradeZone.data.model.rest.chat.ChatRestModel;
import TradeZone.data.model.rest.chat.ReadReceiptRequest;
import TradeZone.data.repository.ChatMessageRepository;
import TradeZone.service.ChatService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/messages")
@CrossOrigin("*")
@AllArgsConstructor
public class MessageController {

    private final ChatMessageRepository messageRepository;

    private final ChatService chatService;

    private final ModelMapper mapper;

    @GetMapping(value = "/{channelId}")
    public Page<ChatRestModel> findMessages(Pageable pageable, @PathVariable("channelId") String channelId) {

        Page<ChatMessage> messages = messageRepository.findAllByChannelOrderByTimestampDesc(channelId, pageable);

        return messages.map(x -> {
                    ChatRestModel model = mapper.map(x, ChatRestModel.class);
                    model.setSender(x.getSender().getUser().getUsername());
                    model.setSenderAvatarUrl(x.getSender().getPhoto().getUrl());
                    return model;
                });
    }

    @PostMapping("/read")
    public void sendReadReceipt(@RequestBody ReadReceiptRequest request) {
        messageRepository.sendReadReceipt(request.getChannel(), request.getUsername());
    }
}
