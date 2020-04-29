package TradeZone.service;

import org.springframework.web.multipart.MultipartFile;
import TradeZone.data.model.rest.ImagesToUploadModel;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.PhotoServiceModel;

import java.util.List;

public interface PhotoService {

    PhotoServiceModel create(String imageContent);

    PhotoServiceModel create(MultipartFile file);

    ResponseMessage uploadAdvertisementPhotos(Long advertisementId, String username, ImagesToUploadModel images);

    ResponseMessage delete(Long id);

    void deleteAll(List<Long> ids);
}
