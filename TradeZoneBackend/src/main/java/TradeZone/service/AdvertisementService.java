package TradeZone.service;

import TradeZone.data.error.exception.AdvertisementNotValidException;
import TradeZone.data.error.exception.EntityNotFoundException;
import TradeZone.data.error.exception.NotAllowedException;
import TradeZone.data.model.rest.*;
import TradeZone.data.model.rest.search.*;
import TradeZone.data.model.service.AdvertisementServiceModel;
import org.springframework.data.domain.Page;

public interface AdvertisementService {

    Page<AdvertisementServiceModel> getAllByFullSearch(FullSearchRequest search);

    AdvertisementServiceModel getById(Long id) throws EntityNotFoundException;

    void create(AdvertisementCreateModel restModel) throws EntityNotFoundException, AdvertisementNotValidException;

    void edit(AdvertisementEditedModel restModel) throws EntityNotFoundException, NotAllowedException;

    void delete(String principalName, DeleteAdvRequest deleteRequest) throws EntityNotFoundException, NotAllowedException;

    void updateViews(Long id, ViewsUpdate views) throws EntityNotFoundException;

    void deletePhoto(DeleteAdvImageRequest deleteRequest) throws EntityNotFoundException;

    Long getCountBySearch(SearchRequest search);
}
