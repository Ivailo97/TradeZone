package TradeZone.data.model.view;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdvertisementDetailsViewModel {

    private Long id;

    private String title;

    private String description;

    private PhotoViewModel[] images;

    private BigDecimal price;

    private String condition;

    private Long views;

    private String creator;
}
