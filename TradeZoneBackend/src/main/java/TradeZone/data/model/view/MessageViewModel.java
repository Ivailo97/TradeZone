package TradeZone.data.model.view;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageViewModel implements Comparable<MessageViewModel> {

    private String senderPhotoUrl;

    private String content;

    private LocalDateTime dateTime;

    @Override
    public int compareTo(MessageViewModel o) {
        return this.getDateTime().compareTo(o.getDateTime());
    }
}
