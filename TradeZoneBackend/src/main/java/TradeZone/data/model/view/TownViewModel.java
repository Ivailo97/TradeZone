package TradeZone.data.model.view;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TownViewModel {

    private String name;

    private List<AdvertisementListViewModel> advertisements;
}
