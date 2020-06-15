package TradeZone.data.model.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegionServiceModel {

    private String name;

    private List<TownServiceModel> towns;
}
