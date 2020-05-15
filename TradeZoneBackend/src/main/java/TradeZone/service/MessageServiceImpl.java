package TradeZone.service;

import TradeZone.data.error.exception.MessageToSendNotValidException;
import TradeZone.data.model.entity.Conversation;
import TradeZone.data.model.entity.Message;
import TradeZone.data.model.entity.UserProfile;
import TradeZone.data.model.rest.search.MessageToSend;
import TradeZone.data.model.service.MessageServiceModel;
import TradeZone.data.repository.ConversationRepository;
import TradeZone.data.repository.MessageRepository;
import TradeZone.data.repository.UserProfileRepository;
import TradeZone.service.validation.MessageToSendValidationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final String INVALID_MESSAGE = "MESSAGE FORMAT NOT VALID";

    private static final String PROFILE_NOT_FOUND = "PROFILE WITH USERNAME %s not found";

    private final MessageToSendValidationService validationService;

    private final ConversationRepository conversationRepository;

    private final MessageRepository messageRepository;

    private final UserProfileRepository userProfileRepository;

    private final ModelMapper mapper;

    @Override
    public MessageServiceModel sendMessage(MessageToSend message) {

        if (!validationService.isValid(message)) {
            throw new MessageToSendNotValidException(INVALID_MESSAGE);
        }

        UserProfile senderProfile = userProfileRepository.findByUserUsername(message.getSender())
                .orElseThrow(supplyNotFoundException(message.getSender()));

        UserProfile receiverProfile = userProfileRepository.findByUserUsername(message.getReceiver())
                .orElseThrow(supplyNotFoundException(message.getReceiver()));

        Conversation senderSide = conversationRepository.getByHostUserUsernameAndInterlocutorUserUsername(message.getSender(), message.getReceiver())
                .orElse(new Conversation(senderProfile, receiverProfile));

        Conversation receiverSide = conversationRepository.getByHostUserUsernameAndInterlocutorUserUsername(message.getReceiver(), message.getSender())
                .orElse(new Conversation(receiverProfile, senderProfile));

        Message entity = new Message(message.getContent(), senderProfile, receiverProfile);

        entity = updateConversations(List.of(senderSide, receiverSide), entity);

        return mapper.map(entity, MessageServiceModel.class);
    }



    private Message updateConversations(List<Conversation> conversations, Message message) {

        conversations.forEach(x -> {
            x.getMessages().add(message);
            message.getConversations().add(x);
        });

        conversationRepository.saveAll(conversations);

        return messageRepository.save(message);
    }

    private Supplier<EntityNotFoundException> supplyNotFoundException(String username) {
        return () -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, username));
    }
}
