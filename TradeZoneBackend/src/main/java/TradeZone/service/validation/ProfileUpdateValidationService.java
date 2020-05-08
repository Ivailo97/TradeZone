package TradeZone.service.validation;

import org.springframework.stereotype.Service;
import TradeZone.data.model.rest.ProfileUpdate;

@Service
public class ProfileUpdateValidationService implements ValidationService<ProfileUpdate> {

    @Override
    public boolean isValid(ProfileUpdate element) {
        return true;
    }
}
