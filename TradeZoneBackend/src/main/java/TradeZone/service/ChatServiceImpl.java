package TradeZone.service;

import TradeZone.data.model.entity.Channel;
import TradeZone.data.model.entity.ChatMessage;
import TradeZone.data.model.entity.UserProfile;
import TradeZone.data.model.rest.chat.ChatRestModel;
import TradeZone.data.repository.ChannelRepository;
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
    private final ChannelRepository channelRepository;
    private final ModelMapper mapper;
    private final ChatMessageRepository messageRepository;
    private final UserProfileRepository profileRepository;

    @Override
    public void saveAndSend(ChatRestModel message) {

        UserProfile sender = profileRepository
                .findByUserUsername(message.getSender())
                .orElseThrow();

        Channel channel = channelRepository.findById(message.getChannelId())
                .orElseThrow();

        message.setTimestamp(new Date());
        message.setSenderAvatarUrl(sender.getPhoto().getUrl());

        ChatMessage entity = new ChatMessage(sender,channel,message.getContent(),message.getTimestamp(),message.getReadDate());
        messageRepository.save(entity);


        template.convertAndSend("/channel/chat/" + message.getChannelId(), message);
    }
}
