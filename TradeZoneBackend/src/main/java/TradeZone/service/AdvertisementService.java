package TradeZone.service;

import TradeZone.data.model.rest.AdvertisementCreateModel;
import TradeZone.data.model.rest.AdvertisementEditedModel;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.AdvertisementServiceModel;

import java.math.BigDecimal;
import java.util.List;

public interface AdvertisementService {

    List<AdvertisementServiceModel> getAllWithPriceBetween(BigDecimal min, BigDecimal max);

    List<AdvertisementServiceModel> getByTitleContainingAndPriceBetween(String title, BigDecimal min, BigDecimal max);

    List<AdvertisementServiceModel> getAllByCategoryAndPriceBetween(String categoryName, BigDecimal min, BigDecimal max);

    AdvertisementServiceModel getById(Long id);

    ResponseMessage create(AdvertisementCreateModel restModel);

    ResponseMessage edit(AdvertisementEditedModel restModel);

    ResponseMessage delete(String principalName, String requestSender, Long id);

    ResponseMessage increaseViews(Long id, Long updatedViews);

    ResponseMessage detachPhoto(String username, Long id, Long photoId);

    List<AdvertisementServiceModel> getAllByCategoryTitleContainingAndPriceBetween(String categoryName, String searchText, BigDecimal min, BigDecimal max);
}
