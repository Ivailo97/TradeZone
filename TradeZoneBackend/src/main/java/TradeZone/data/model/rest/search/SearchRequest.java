package TradeZone.data.model.rest.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest extends ConditionSearch {

    private String category;

    private String search;
}
