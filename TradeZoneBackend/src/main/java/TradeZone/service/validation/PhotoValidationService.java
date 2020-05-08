package TradeZone.service.validation;

import org.springframework.stereotype.Service;

@Service
public class PhotoValidationService implements ValidationService<String> {

    @Override
    public boolean isValid(String imageContent) {
        return true;
    }
}
