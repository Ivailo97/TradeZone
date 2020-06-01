package TradeZone.data.model.view;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChatMessageViewModel {

    private String channelId;

    private String sender;

    private String content;

    private Date timestamp;

    private Date readDate;
}
