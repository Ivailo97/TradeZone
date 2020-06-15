package TradeZone.service;

import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.service.CategoryServiceModel;
import TradeZone.data.model.service.ProfileServiceModel;
import TradeZone.data.model.service.RegionServiceModel;
import TradeZone.data.model.view.*;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.function.Function;

public interface MappingService {

    ModelMapper getMapper();

    List<AdvertisementListViewModel> mapServiceAdvertisementsToView(List<AdvertisementServiceModel> models);

    List<ProfileTableViewModel> mapServiceToTableViewModel(List<ProfileServiceModel> models, ProfileService service);

    List<RegionViewModel> mapServiceRegionToView(List<RegionServiceModel> models);

    ProfileViewModel mapServiceProfileToView(ProfileServiceModel model);

    Function<CategoryServiceModel, TopCategoryViewModel> serviceToTopCategoryViewModel();
}

