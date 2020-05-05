package TradeZone.data.model.service;

import lombok.Getter;
import lombok.Setter;
import TradeZone.data.model.enums.Condition;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AdvertisementServiceModel extends BaseServiceModel {

    private Long views;

    private String title;

    private BigDecimal price;

    private String description;

    private Condition condition;

    private ProfileServiceModel creator;

    private CategoryServiceModel category;

    private List<PhotoServiceModel> photos;

    private List<ProfileServiceModel> profilesWhichViewedIt;

    private List<ProfileServiceModel> profilesWhichLikedIt;
}
