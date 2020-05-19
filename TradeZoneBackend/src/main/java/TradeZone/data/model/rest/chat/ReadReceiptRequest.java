package TradeZone.data.model.rest.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReadReceiptRequest {

    private String channel;
    private String username;
}
