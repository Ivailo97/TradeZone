package TradeZone.service.validation;

import TradeZone.data.model.rest.search.SearchRequest;
import org.springframework.stereotype.Service;

@Service
public class SearchRequestValidationService implements ValidationService<SearchRequest> {

    @Override
    public boolean isValid(SearchRequest searchRequest) {
        return fieldsArePresent(searchRequest);
    }

    private boolean fieldsArePresent(SearchRequest searchRequest) {
        return searchRequest.getCategory() != null && searchRequest.getSearch() != null
                && searchRequest.getCondition() != null && searchRequest.getMax() != null
                && searchRequest.getMin() != null;
    }
}
