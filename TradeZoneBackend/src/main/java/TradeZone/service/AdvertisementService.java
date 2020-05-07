package TradeZone.service;

import TradeZone.data.model.rest.*;
import TradeZone.data.model.rest.search.*;
import TradeZone.data.model.service.AdvertisementServiceModel;
import org.springframework.data.domain.Page;

public interface AdvertisementService {

    Page<AdvertisementServiceModel> getAllByFullSearch(FullSearchRequest search);

    AdvertisementServiceModel getById(Long id);

    Long getCountBySearch(SearchRequest search);

    AdvertisementServiceModel create(AdvertisementCreateModel restModel);

    AdvertisementServiceModel edit(AdvertisementEditedModel restModel);

    void delete(String principalName, DeleteAdvRequest deleteRequest);

    void updateViews(Long id, ViewsUpdate views);

    void deletePhoto(DeleteAdvImageRequest deleteRequest);
}
