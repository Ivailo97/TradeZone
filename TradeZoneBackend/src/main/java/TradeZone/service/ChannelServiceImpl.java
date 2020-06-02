package TradeZone.service;

import TradeZone.data.error.exception.EntityNotFoundException;
import TradeZone.data.error.exception.NotAllowedException;
import TradeZone.data.model.entity.Channel;
import TradeZone.data.model.entity.UserProfile;
import TradeZone.data.model.rest.Subscription;
import TradeZone.data.model.service.ChannelServiceModel;
import TradeZone.data.repository.ChannelRepository;
import TradeZone.data.repository.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;

    private final UserProfileRepository userProfileRepository;

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

    @Override
    public ChannelServiceModel subscribeToChannel(Subscription subscription) {

        UserProfile profile = userProfileRepository.findByUserUsername(subscription.getUsername()).orElseThrow();

        Channel channel = channelRepository.findById(subscription.getChannelId()).orElseThrow();

        if (subscribed(profile, channel)) {
            throw new NotAllowedException("invalid");
        }

        profile.getSubscribedTo().add(channel);

        profile = userProfileRepository.save(profile);

        return mapper.map(profile.getSubscribedTo().stream().filter(x -> x.getId().equals(channel.getId())).findFirst(), ChannelServiceModel.class);
    }

    @Override
    public void unsubscribeFromChannel(Subscription subscription) {
        UserProfile profile = userProfileRepository.findByUserUsername(subscription.getUsername()).orElseThrow();

        Channel channel = channelRepository.findById(subscription.getChannelId()).orElseThrow();

        if (!subscribed(profile, channel)) {
            throw new NotAllowedException("invalid");
        }

        profile.setSubscribedTo(profile.getSubscribedTo()
                .stream()
                .filter(x -> !x.getId().equals(channel.getId()))
                .collect(Collectors.toList()));

        userProfileRepository.save(profile);
    }

    private boolean subscribed(UserProfile profile, Channel channel) {
        return profile.getSubscribedTo().stream().anyMatch(x -> x.getId().equals(channel.getId()));
    }
}
