package TradeZone.data.model.service.validation;

import org.springframework.stereotype.Service;
import TradeZone.data.model.service.AdvertisementServiceModel;

@Service
public class AdvertisementValidationService implements ValidationService<AdvertisementServiceModel> {

    @Override
    public boolean isValid(AdvertisementServiceModel advertisement) {
        return true;
    }
}
