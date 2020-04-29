package TradeZone.data.model.service.validation;

import org.springframework.stereotype.Service;
import TradeZone.data.model.service.CategoryServiceModel;

@Service
public class CategoryValidationService implements ValidationService<CategoryServiceModel> {

    @Override
    public boolean isValid(CategoryServiceModel element) {
        return true;
    }
}
