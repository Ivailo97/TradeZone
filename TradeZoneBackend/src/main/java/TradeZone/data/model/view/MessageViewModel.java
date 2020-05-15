package TradeZone.data.model.view;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageViewModel {

    private String senderPhotoUrl;

    private String content;

    private LocalDateTime dateTime;
}
