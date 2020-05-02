package TradeZone.data.model.rest.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlmostFullSearchRequest extends CategorySearchRequest {

    private String sortBy;

    private String order;

    private Integer page;
}
