package TradeZone.data.model.rest.search;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SearchRequest extends ConditionSearch {

    private String category;

    private String search;
}
