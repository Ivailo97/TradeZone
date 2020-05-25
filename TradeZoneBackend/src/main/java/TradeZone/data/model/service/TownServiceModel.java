package TradeZone.data.model.service;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TownServiceModel extends BaseServiceModel {

    private String name;

    private String region;

    private List<ProfileServiceModel> citizen;

    public TownServiceModel() {
        this.citizen = new ArrayList<>();
    }
}
