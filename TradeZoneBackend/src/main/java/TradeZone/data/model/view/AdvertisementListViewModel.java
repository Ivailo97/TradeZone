package TradeZone.data.model.view;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AdvertisementListViewModel {

    private Long id;

    private Long views;

    private String title;

    private String imageUrl;

    private String condition;

    private BigDecimal price;

    private String description;

    private String creator;

    private List<String> profilesWhichLikedIt;
}
