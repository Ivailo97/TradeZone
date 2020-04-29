package TradeZone.data.model.view;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TopCategoryViewModel extends CategoryListViewModel {

    private List<AdvertisementListViewModel> advertisements;
}
