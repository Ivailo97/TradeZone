package TradeZone.service;

import com.cloudinary.Uploader;
import lombok.AllArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import TradeZone.data.model.entity.Advertisement;
import TradeZone.data.model.entity.Photo;
import TradeZone.data.model.rest.ImagesToUploadModel;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.PhotoServiceModel;
import TradeZone.data.model.service.validation.PhotoValidationService;
import TradeZone.data.repository.AdvertisementRepository;
import TradeZone.data.repository.PhotoRepository;

import java.io.File;
import java.util.*;

@Service
@AllArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private static final String FAIL = "FAIL";
    private static final String SUCCESS = "SUCCESS";

    private final PhotoRepository photoRepository;

    private final AdvertisementRepository advertisementRepository;

    private final Uploader uploader;

    private final PhotoValidationService validationService;

    @Override
    public PhotoServiceModel create(String imageContent) {

        if (!validationService.isValid(imageContent)) {
            throw new IllegalArgumentException();
        }

        Pair<String, String> cloudUploadData = uploadToCloud(imageContent);

        PhotoServiceModel photoServiceModel = new PhotoServiceModel();
        photoServiceModel.setUrl(cloudUploadData.getLeft());
        photoServiceModel.setIdInCloud(cloudUploadData.getRight());

        return photoServiceModel;
    }

    @Override
    public PhotoServiceModel create(MultipartFile multipartFile) {

        Pair<String, String> cloudUploadData = uploadToCloud(multipartFile);

        PhotoServiceModel photoServiceModel = new PhotoServiceModel();
        photoServiceModel.setUrl(cloudUploadData.getLeft());
        photoServiceModel.setIdInCloud(cloudUploadData.getRight());

        return photoServiceModel;
    }

    @Override
    public ResponseMessage uploadAdvertisementPhotos(Long advertisementId, String username, ImagesToUploadModel model) {

        if (Arrays.stream(model.getImages()).anyMatch(x -> !validationService.isValid(x))) {
            return new ResponseMessage(FAIL);
        }

        Advertisement advertisement = advertisementRepository.findById(advertisementId).orElse(null);

        if (advertisement == null || !advertisement.getCreator().getUser().getUsername().equals(username)) {
            return new ResponseMessage(FAIL);
        }

        List<Photo> photos = new ArrayList<>();

        for (String content : model.getImages()) {
            Pair<String, String> cloudUploadData = uploadToCloud(content);
            Photo photo = new Photo();
            photo.setUrl(cloudUploadData.getLeft());
            photo.setIdInCloud(cloudUploadData.getRight());
            photos.add(photo);
        }

        advertisement.getPhotos().addAll(photos);

        advertisementRepository.save(advertisement);

        return new ResponseMessage(SUCCESS);
    }

    @Override
    public ResponseMessage delete(Long id) {

        try {
            Photo photo = photoRepository.findById(id).orElse(null);
            uploader.destroy(photo.getIdInCloud(), new HashMap<>());
            photoRepository.delete(photo);
            return new ResponseMessage(SUCCESS);
        } catch (Exception ignored) {
            return new ResponseMessage(FAIL);
        }
    }

    @Override
    public void deleteAll(List<Long> ids) {

        try {

            List<Photo> photos = photoRepository.findAllById(ids);
            for (Photo photo : photos) {
                uploader.destroy(photo.getIdInCloud(), new HashMap<>());
            }
            photoRepository.deleteAll(photos);

        } catch (Exception ignored) {

        }
    }

    private Pair<String, String> uploadToCloud(String content) {

        Map<String, String> uploadedData;

        try {
            uploadedData = uploader.upload(content, new HashMap<>());
        } catch (Exception ignored) {
            uploadedData = null;
        }

        //cloud works this way
        return Pair.of(uploadedData.get("url"), uploadedData.get("public_id"));
    }

    private Pair<String, String> uploadToCloud(MultipartFile multipartFile) {

        Map<String, String> uploadedData;

        try {
            File file = File.createTempFile("temp-file", multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
            uploadedData = uploader.upload(file, new HashMap());
        } catch (Exception ex) {
            uploadedData = null;
        }

        //cloud works this way
        return Pair.of(uploadedData.get("url"), uploadedData.get("public_id"));
    }
}
