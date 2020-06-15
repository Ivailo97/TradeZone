package TradeZone.service;

import TradeZone.data.model.service.RegionServiceModel;
import TradeZone.data.model.service.TownServiceModel;

import java.util.List;

public interface RegionService {

    List<TownServiceModel> getAllTowns();

    List<RegionServiceModel> getAllRegions();
}
