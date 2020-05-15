package TradeZone.data.model.service;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageServiceModel extends BaseServiceModel {

    private String content;

    private ProfileServiceModel sender;

    private ProfileServiceModel receiver;

    private LocalDateTime dateTime;
}
