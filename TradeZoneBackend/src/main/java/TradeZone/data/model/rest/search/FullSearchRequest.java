package TradeZone.data.model.rest.search;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FullSearchRequest extends SearchRequest {


    private Integer page;

    private String sortBy;

    private String order;
}
