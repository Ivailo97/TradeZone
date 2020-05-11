package TradeZone.data.model.view;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AdvertisementDetailsViewModel {

    private Long id;

    private String title;

    private LocalDateTime createdOn;

    private String description;

    private PhotoViewModel[] images;

    private BigDecimal price;

    private String condition;

    private String delivery;

    private String categoryName;

    private Long views;

    private ProfileCreatorViewModel creator;
}
