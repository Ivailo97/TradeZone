package TradeZone.data.model.service;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChatMessageServiceModel extends BaseServiceModel {

    private ProfileServiceModel sender;

    private ChannelServiceModel channel;

    private String content;

    private Date timestamp;

    private Date readDate;
}
