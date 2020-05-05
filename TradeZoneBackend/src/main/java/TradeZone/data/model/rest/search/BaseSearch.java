package TradeZone.data.model.rest.search;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@Setter
public class BaseSearch {

    private BigDecimal min;

    private BigDecimal max;
}
