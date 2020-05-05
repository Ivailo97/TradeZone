package TradeZone.data.model.service.validation;

import TradeZone.data.model.rest.AdvertisementBaseModel;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementValidationService implements ValidationService<AdvertisementBaseModel> {

    // edit and create
    @Override
    public boolean isValid(AdvertisementBaseModel advertisement) {
        return fieldsArePresent(advertisement) && fieldsAreValid(advertisement);
    }

    private boolean fieldsArePresent(AdvertisementBaseModel model) {
        return model.getCreator() != null && model.getCondition() != null && model.getCategory() != null
                && model.getDescription() != null && model.getPrice() != null && model.getTitle() != null;
    }

    //TODO
    private boolean fieldsAreValid(AdvertisementBaseModel advertisement) {
        return true;
    }
}
