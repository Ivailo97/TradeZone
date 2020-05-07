package TradeZone.data.model.rest;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdvertisementBaseModel {

    private String title;

    private String description;

    private BigDecimal price;

    private String condition;

    private String delivery;

    private Long category;

    private String creator;
}
