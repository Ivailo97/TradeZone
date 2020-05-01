package TradeZone.service;

import TradeZone.data.model.rest.AdvertisementCreateModel;
import TradeZone.data.model.rest.AdvertisementEditedModel;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.AdvertisementServiceModel;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

public interface AdvertisementService {

    AdvertisementServiceModel getById(Long id);

    Long countOfAll();

    Long countByCategoryAndPriceBetween(String category, BigDecimal min, BigDecimal max);

    Long countOfPriceBetween(BigDecimal min, BigDecimal max);

    Long countOfPriceBetweenAndCondition(BigDecimal min, BigDecimal max, String condition);

    Long countByCategoryConditionAndPriceBetween(String category, String condition, BigDecimal min, BigDecimal max);

    ResponseMessage create(AdvertisementCreateModel restModel);

    ResponseMessage edit(AdvertisementEditedModel restModel);

    ResponseMessage delete(String principalName, String requestSender, Long id);

    ResponseMessage increaseViews(Long id, Long updatedViews);

    ResponseMessage detachPhoto(String username, Long id, Long photoId);

    List<AdvertisementServiceModel> getAllByCategoryTitleContainingAndPriceBetween(String categoryName, String searchText, BigDecimal min, BigDecimal max, PageRequest pageable);

    List<AdvertisementServiceModel> getAllByCategoryPriceBetweenAndCondition(BigDecimal min, BigDecimal max, String condition, String category, PageRequest pageRequest);

    List<AdvertisementServiceModel> getAllByCategoryAndPriceBetween(String category, BigDecimal min, BigDecimal max, PageRequest pageRequest);

    List<AdvertisementServiceModel> getAllByCategoryTitleContainingPriceBetweenAndCondition(String category, String search, BigDecimal min, BigDecimal max, String condition, PageRequest pageRequest);

    Long countByCategoryTitleContainingPriceBetweenAndCondition(String category, String search, BigDecimal min, BigDecimal max, String condition);
}
