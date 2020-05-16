package TradeZone.data.model.view;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class ConversationViewModel {

    private Long id;

    private ProfileConversationViewModel interlocutor;

    private ProfileConversationViewModel host;

    private Collection<MessageViewModel> messages;

    public ConversationViewModel() { }
}
