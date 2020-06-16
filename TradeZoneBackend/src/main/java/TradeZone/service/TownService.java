package TradeZone.service;

import TradeZone.data.model.service.TownServiceModel;

import java.util.List;

public interface TownService {

    List<TownServiceModel> getAllTowns();

    List<TownServiceModel> getAllInRegion(String regionName);
}
