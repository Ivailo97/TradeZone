package TradeZone.service;

import TradeZone.data.model.rest.*;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.rest.search.*;
import TradeZone.data.model.service.AdvertisementServiceModel;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

public interface AdvertisementService {

    AdvertisementServiceModel getById(Long id);

    Long countOfAll();

    Long countOfPriceBetween(BaseSearch baseSearch);

    ResponseMessage create(AdvertisementCreateModel restModel);

    ResponseMessage edit(AdvertisementEditedModel restModel);

    ResponseMessage delete(String principalName, String requestSender, Long id);

    ResponseMessage increaseViews(Long id, Long updatedViews);

    ResponseMessage detachPhoto(String username, Long id, Long photoId);

    Long countByCategoryTitleContainingPriceBetweenAndCondition(SearchRequest search);

    List<AdvertisementServiceModel> getAllByAlmostFullSearch(AlmostFullSearchRequest search);

    Long countByPriceBetweenAndCondition(ConditionSearch search);

    Long countByCategory(CategorySearchRequest search);

    List<AdvertisementServiceModel> getAllByFullSearch(FullSearchRequest search);
}
