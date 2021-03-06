package TradeZone.web.controller;

import TradeZone.data.model.entity.ChatMessage;
import TradeZone.data.model.rest.chat.ChatRestModel;
import TradeZone.data.model.rest.chat.ReadReceiptRequest;
import TradeZone.data.repository.ChatMessageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/messages")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@AllArgsConstructor
public class MessageController {

    private final ChatMessageRepository messageRepository;

    private final ModelMapper mapper;

    @GetMapping("/{channelId}")
    public Page<ChatRestModel> findMessages(Pageable pageable, @PathVariable("channelId") String channelId) {

        Page<ChatMessage> messages = messageRepository.findAllByChannelIdOrderByTimestampDesc(channelId, pageable);

        return messages.map(x -> {
            ChatRestModel model = mapper.map(x, ChatRestModel.class);
            model.setSender(x.getSender().getUser().getUsername());
            model.setChannelId(x.getChannel().getId());
            model.setSenderAvatarUrl(x.getSender().getPhoto().getUrl());
            return model;
        });
    }

    @PostMapping("/read")
    public void sendReadReceipt(@RequestBody ReadReceiptRequest request) {
        messageRepository.sendReadReceipt(request.getChannelId(), request.getUsername());
    }
}
