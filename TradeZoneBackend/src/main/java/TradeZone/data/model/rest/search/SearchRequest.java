package TradeZone.data.model.rest.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SearchRequest extends ConditionSearch {

    private String category;

    private String search;

    public SearchRequest(BigDecimal min, BigDecimal max,
                         String condition, String category,
                         String search) {

        super(min, max, condition);
        this.category = category;
        this.search = search;
    }
}
