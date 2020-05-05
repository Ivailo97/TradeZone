package TradeZone.data.model.rest.search;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ConditionSearch extends BaseSearch {

    private String condition;
}
