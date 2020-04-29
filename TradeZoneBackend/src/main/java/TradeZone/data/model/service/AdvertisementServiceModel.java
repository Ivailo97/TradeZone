package TradeZone.data.model.service;

import lombok.Getter;
import lombok.Setter;
import TradeZone.data.model.enums.Condition;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AdvertisementServiceModel {

    private Long id;

    private ProfileServiceModel creator;

    private List<ProfileServiceModel> profilesWhichLikedIt;

    private String title;

    private String description;

    private BigDecimal price;

    private List<PhotoServiceModel> photos;

    private Condition condition;

    private CategoryServiceModel category;

    private Long views;
}
