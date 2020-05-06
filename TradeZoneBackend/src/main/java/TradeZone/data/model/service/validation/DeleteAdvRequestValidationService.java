package TradeZone.data.model.service.validation;

import TradeZone.data.model.rest.DeleteAdvRequest;
import org.springframework.stereotype.Service;

@Service
public class DeleteAdvRequestValidationService implements ValidationService<DeleteAdvRequest> {

    @Override
    public boolean isValid(DeleteAdvRequest element) {
        return fieldsArePresent(element);
    }

    private boolean fieldsArePresent(DeleteAdvRequest element) {
        return element.getAdvertisementId() != null && element.getUsername() != null;
    }
}
