package TradeZone.service;

import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.view.AdvertisementListViewModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MappingServiceImpl implements MappingService {

    private final ModelMapper mapper;

    @Override
    public ModelMapper getMapper() {
        return mapper;
    }

    @Override
    public List<AdvertisementListViewModel> mapServiceAdvertisementsToView(List<AdvertisementServiceModel> models) {
        return models.stream().map(a -> {
            AdvertisementListViewModel model = mapper.map(a, AdvertisementListViewModel.class);

            if (a.getPhotos().size() != 0) {
                model.setImageUrl(a.getPhotos().get(0).getUrl());
            }

            model.setCreator(a.getCreator().getUser().getUsername());
            model.setProfilesWhichLikedIt(a.getProfilesWhichLikedIt().stream().map(x -> x.getUser().getUsername()).collect(Collectors.toList()));
            return model;
        }).collect(Collectors.toList());
    }
}
