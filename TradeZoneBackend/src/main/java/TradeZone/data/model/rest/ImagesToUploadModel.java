package TradeZone.data.model.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagesToUploadModel extends DeleteAdvRequest {

    private String[] images;
}
