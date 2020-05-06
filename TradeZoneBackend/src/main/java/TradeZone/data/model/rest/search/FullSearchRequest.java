package TradeZone.data.model.rest.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
public class FullSearchRequest extends SearchRequest {

    private Integer page;

    private String sortBy;

    private String order;

    public FullSearchRequest(BigDecimal min, BigDecimal max,
                             String condition, String category,
                             String search, Integer page,
                             String sortBy, String order) {

        super(min, max, condition, category, search);
        this.page = page;
        this.sortBy = sortBy;
        this.order = order;
    }
}
