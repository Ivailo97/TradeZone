package TradeZone.service;

import TradeZone.data.model.rest.chat.ChatRestModel;

public interface ChatService {

    void saveAndSend(ChatRestModel message);
}
