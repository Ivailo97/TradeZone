package TradeZone.service;

import TradeZone.data.error.exception.EntityNotFoundException;
import TradeZone.data.model.rest.*;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.rest.search.*;
import TradeZone.data.model.service.AdvertisementServiceModel;
import org.springframework.data.domain.Page;

public interface AdvertisementService {

    Page<AdvertisementServiceModel> getAllByAlmostFullSearch(AlmostFullSearchRequest search);

    Page<AdvertisementServiceModel> getAllByFullSearch(FullSearchRequest search);

    AdvertisementServiceModel getById(Long id) throws EntityNotFoundException;

    ResponseMessage create(AdvertisementCreateModel restModel);

    ResponseMessage edit(AdvertisementEditedModel restModel);

    ResponseMessage delete(String principalName, String requestSender, Long id);

    void updateViews(Long id, ViewsUpdate views) throws EntityNotFoundException;

    void detachPhoto(String username, Long id, Long photoId) throws EntityNotFoundException;

    Long countOfPriceBetween(BaseSearch baseSearch);

    Long countByCategoryTitleContainingPriceBetweenAndCondition(SearchRequest search);

    Long countByPriceBetweenAndCondition(ConditionSearch search);

    Long countByCategory(CategorySearchRequest search);
}
