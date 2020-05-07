package TradeZone.data.model.view;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdvertisementToEditViewModel {

    private String title;

    private String description;

    private BigDecimal price;

    private String condition;

    private String delivery;

    private Long categoryId;

    private String creator;
}
