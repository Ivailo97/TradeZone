package TradeZone.service;

import TradeZone.data.model.service.*;
import TradeZone.data.model.view.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
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

    @Override
    public List<ProfileTableViewModel> mapServiceToTableViewModel(List<ProfileServiceModel> models, ProfileService service) {
        return models.stream()
                .map(x -> {
                    ProfileTableViewModel viewModel = mapper.map(x, ProfileTableViewModel.class);
                    viewModel.setRole(service.getTopRole(x.getUser().getRoles()));
                    return viewModel;
                }).collect(Collectors.toList());
    }

    @Override
    public List<RegionViewModel> mapServiceRegionToView(List<RegionServiceModel> models) {
        return models.stream()
                .map(x -> {
                    RegionViewModel viewModel = mapper.map(x, RegionViewModel.class);
                    Long count = x.getTowns().stream()
                            .map(t -> t.getCitizen().stream().map(c -> c.getCreatedAdvertisements().size())
                                    .mapToInt(Integer::intValue).sum())
                            .mapToLong(Integer::longValue)
                            .sum();
                    viewModel.setTotalAds(count);
                    return viewModel;
                }).collect(Collectors.toList());
    }

    @Override
    public ProfileViewModel mapServiceProfileToView(ProfileServiceModel model) {
        ProfileViewModel viewModel = mapper.map(model, ProfileViewModel.class);
        viewModel.setCreatedAdvertisements(mapServiceAdvertisementsToView(model.getCreatedAdvertisements()));
        viewModel.setFavorites(mapServiceAdvertisementsToView(model.getFavorites()));
        viewModel.setSubscribedTo(model.getSubscribedTo().stream().map(ChannelServiceModel::getId).collect(Collectors.toList()));
        viewModel.setRoles(model.getUser().getRoles().stream().map(r -> r.getRoleName().name()).collect(Collectors.toList()));
        return viewModel;
    }

    @Override
    public Function<CategoryServiceModel, TopCategoryViewModel> serviceToTopCategoryViewModel() {
        return x -> {
            TopCategoryViewModel viewModel = mapper.map(x, TopCategoryViewModel.class);
            viewModel.setAdvertisements(mapServiceAdvertisementsToView(x.getAdvertisements()));
            return viewModel;
        };
    }
}
