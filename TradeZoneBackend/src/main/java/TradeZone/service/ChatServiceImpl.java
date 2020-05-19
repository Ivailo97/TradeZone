package TradeZone.service;

import TradeZone.data.model.entity.ChatMessage;
import TradeZone.data.model.entity.UserProfile;
import TradeZone.data.model.rest.chat.ChatRestModel;
import TradeZone.data.model.service.ChatMessageServiceModel;
import TradeZone.data.repository.ChatMessageRepository;
import TradeZone.data.repository.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final SimpMessagingTemplate template;
    private final ModelMapper mapper;
    private final ChatMessageRepository messageRepository;
    private final UserProfileRepository profileRepository;

    @Override
    public void saveAndSend(ChatRestModel message) {

        UserProfile sender = profileRepository
                .findByUserUsername(message.getSender())
                .orElseThrow();

        message.setTimestamp(new Date());
        message.setSenderAvatarUrl(sender.getPhoto().getUrl());
        ChatMessage entity = mapper.map(message, ChatMessage.class);

        entity.setSender(sender);
        messageRepository.save(entity);

        template.convertAndSend("/channel/chat/" + message.getChannel(), message);
    }
}
