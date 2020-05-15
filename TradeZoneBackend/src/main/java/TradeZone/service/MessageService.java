package TradeZone.service;

import TradeZone.data.model.rest.search.MessageToSend;
import TradeZone.data.model.service.MessageServiceModel;

public interface MessageService {

   MessageServiceModel sendMessage(MessageToSend message);
}
