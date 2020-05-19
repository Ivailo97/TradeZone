package TradeZone.data.model.rest.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRestModel {

    private String sender;

    private String channel;

    private String content;

    private Date timestamp;

    private Date readDate;

    private String senderAvatarUrl;
}