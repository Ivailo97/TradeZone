package TradeZone.data.model.rest;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TownBindingModel {

    @Expose
    private String name;

    @Expose
    private String region;
}
