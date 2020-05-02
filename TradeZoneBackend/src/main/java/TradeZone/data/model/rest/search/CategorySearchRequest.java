package TradeZone.data.model.rest.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorySearchRequest extends ConditionSearch {

    private String category;
}
