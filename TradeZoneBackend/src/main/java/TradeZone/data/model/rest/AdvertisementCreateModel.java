package TradeZone.data.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdvertisementCreateModel extends AdvertisementBaseModel {

    private String[] images;

}
