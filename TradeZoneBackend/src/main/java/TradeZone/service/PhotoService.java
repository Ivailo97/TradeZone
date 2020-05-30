package TradeZone.service;

import org.springframework.web.multipart.MultipartFile;
import TradeZone.data.model.rest.ImagesToUploadModel;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.PhotoServiceModel;

import java.util.List;

public interface PhotoService {

    PhotoServiceModel upload(String imageContent);

    PhotoServiceModel upload(MultipartFile file);

    List<PhotoServiceModel> uploadAdvertisementPhotos(ImagesToUploadModel images);

    ResponseMessage delete(Long id);

    void deleteAll(List<Long> ids);
}
