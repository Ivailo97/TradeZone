package TradeZone.data.model.rest;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdvertisementCreateModel {

    private String title;

    private String creator;

    private String description;

    private String[] images;

    private BigDecimal price;

    private String condition;

    //id
    private Long category;
}
