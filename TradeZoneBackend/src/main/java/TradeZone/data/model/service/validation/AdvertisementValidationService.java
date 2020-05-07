package TradeZone.data.model.service.validation;

import TradeZone.data.model.enums.Condition;
import TradeZone.data.model.enums.DeliveryType;
import TradeZone.data.model.rest.AdvertisementBaseModel;
import TradeZone.data.model.rest.AdvertisementCreateModel;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementValidationService implements ValidationService<AdvertisementBaseModel> {

    private static final String TITLE_MATCHER = "^[A-ZА-Я][A-ZА-Яа-яa-z\\s\\d]{4,19}$";

    @Override
    public boolean isValid(AdvertisementBaseModel advertisement) {
        return fieldsArePresent(advertisement) && fieldsAreValid(advertisement);
    }

    private boolean fieldsArePresent(AdvertisementBaseModel model) {
        return model.getCreator() != null && model.getCondition() != null && model.getCategory() != null
                && model.getDescription() != null && model.getPrice() != null && model.getTitle() != null;
    }

    private boolean fieldsAreValid(AdvertisementBaseModel advertisement) {

        if (!advertisement.getTitle().matches(TITLE_MATCHER)) {
            return false;
        }

        if (isCreateModelAndHasNoImages(advertisement)) {
            return false;
        }

        try {
            Condition.valueOf(advertisement.getCondition());
            DeliveryType.valueOf(advertisement.getDelivery());
        } catch (IllegalArgumentException ex) {
            return false;
        }

        return true;
    }

    private boolean isCreateModelAndHasNoImages(AdvertisementBaseModel advertisement) {

        return advertisement.getClass().getSimpleName().equals("AdvertisementCreateModel")
                && ((AdvertisementCreateModel) advertisement).getImages().length < 1;
    }
}
