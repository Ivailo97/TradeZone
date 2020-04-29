package TradeZone.data.model.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryServiceModel {

    private Long id;

    private ProfileServiceModel creator;

    private String name;

    private PhotoServiceModel photo;

    private List<AdvertisementServiceModel> advertisements;
}
