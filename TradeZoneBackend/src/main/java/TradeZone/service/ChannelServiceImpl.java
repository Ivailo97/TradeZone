package TradeZone.service;

import TradeZone.data.error.exception.EntityNotFoundException;
import TradeZone.data.model.entity.Channel;
import TradeZone.data.model.service.ChannelServiceModel;
import TradeZone.data.repository.ChannelRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;

    private final ModelMapper mapper;

    @Override
    public ChannelServiceModel create(String id) {

        if (channelRepository.existsById(id)) {
            throw new EntityNotFoundException("invalid");
        }

        Channel channel = new Channel(id);

        channel = channelRepository.save(channel);

        return mapper.map(channel, ChannelServiceModel.class);
    }
}
