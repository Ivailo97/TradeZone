package TradeZone.service;

import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.service.CategoryServiceModel;
import TradeZone.data.model.service.ProfileServiceModel;
import TradeZone.data.model.view.AdvertisementListViewModel;
import TradeZone.data.model.view.ProfileTableViewModel;
import TradeZone.data.model.view.ProfileViewModel;
import TradeZone.data.model.view.TopCategoryViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;


public interface MappingService {

    ModelMapper getMapper();

    List<AdvertisementListViewModel> mapServiceAdvertisementsToView(List<AdvertisementServiceModel> models);

    List<ProfileTableViewModel> mapServiceToTableViewModel(List<ProfileServiceModel> models, ProfileService service);

    ProfileViewModel mapServiceProfileToView(ProfileServiceModel model);

    Function<CategoryServiceModel, TopCategoryViewModel> serviceToTopCategoryViewModel();
}

