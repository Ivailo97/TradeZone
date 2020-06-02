package TradeZone.service;

import TradeZone.data.model.rest.Subscription;
import TradeZone.data.model.service.ChannelServiceModel;

public interface ChannelService {

    ChannelServiceModel create(String id);

    ChannelServiceModel subscribeToChannel(Subscription subscription);

    void unsubscribeFromChannel(Subscription subscription);
}
