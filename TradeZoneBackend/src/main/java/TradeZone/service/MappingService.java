package TradeZone.service;

import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.view.AdvertisementListViewModel;
import org.modelmapper.ModelMapper;

import java.util.List;

public interface MappingService {

    ModelMapper getMapper();

    List<AdvertisementListViewModel> mapServiceAdvertisementsToView(List<AdvertisementServiceModel> models);

}

