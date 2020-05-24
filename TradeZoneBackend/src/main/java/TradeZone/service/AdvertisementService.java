package TradeZone.service;

import TradeZone.data.model.rest.*;
import TradeZone.data.model.rest.search.*;
import TradeZone.data.model.service.AdvertisementServiceModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdvertisementService {

    Page<AdvertisementServiceModel> getAllByFullSearch(FullSearchRequest search);

    Long getCountBySearch(SearchRequest search);

    AdvertisementServiceModel getById(Long id);

    AdvertisementServiceModel create(AdvertisementCreateModel restModel);

    AdvertisementServiceModel edit(AdvertisementEditedModel restModel);

    AdvertisementServiceModel delete(String principalName, DeleteAdvRequest deleteRequest);

    AdvertisementServiceModel updateViews(Long id, ViewsUpdate views);

    AdvertisementServiceModel deletePhoto(DeleteAdvImageRequest deleteRequest);

    List<AdvertisementServiceModel> findByCreatorUsernameExcept(SpecificSearch specificSearch, String username);
}
