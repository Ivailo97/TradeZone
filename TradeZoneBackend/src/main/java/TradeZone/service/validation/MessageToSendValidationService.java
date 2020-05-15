package TradeZone.service.validation;

import TradeZone.data.model.rest.search.MessageToSend;
import org.springframework.stereotype.Service;

@Service
public class MessageToSendValidationService implements ValidationService<MessageToSend> {
    @Override
    public boolean isValid(MessageToSend element) {
        return true;
    }
}
