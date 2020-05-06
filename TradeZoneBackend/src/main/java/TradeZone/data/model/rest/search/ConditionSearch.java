package TradeZone.data.model.rest.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ConditionSearch extends BaseSearch {

    private String condition;

    public ConditionSearch(BigDecimal min, BigDecimal max, String condition) {
        super(min, max);
        this.condition = condition;
    }

}
