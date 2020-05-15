package TradeZone.data.model.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConversationServiceModel extends BaseServiceModel {

    private ProfileServiceModel interlocutor;

    private ProfileServiceModel host;

    private List<MessageServiceModel> messages;
}
