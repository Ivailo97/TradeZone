package TradeZone.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import TradeZone.data.model.entity.Advertisement;
import TradeZone.data.model.entity.Category;
import TradeZone.data.model.entity.Photo;
import TradeZone.data.model.entity.UserProfile;
import TradeZone.data.model.enums.Condition;
import TradeZone.data.model.rest.AdvertisementCreateModel;
import TradeZone.data.model.rest.AdvertisementEditedModel;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.service.validation.AdvertisementValidationService;
import TradeZone.data.repository.AdvertisementRepository;
import TradeZone.data.repository.CategoryRepository;
import TradeZone.data.repository.PhotoRepository;
import TradeZone.data.repository.UserProfileRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private static final String FAIL = "FAIL";
    private static final String SUCCESS = "SUCCESS";

    private final AdvertisementRepository advertisementRepository;

    private final AdvertisementValidationService validationService;

    private final PhotoService photoService;

    private final ModelMapper modelMapper;

    private final UserProfileRepository userProfileRepository;

    private final CategoryRepository categoryRepository;

    private final PhotoRepository photoRepository;

    private void seedCategories() {

        if (categoryRepository.count() != 0) {
            return;
        }

        Photo photo = new Photo();
        photo.setUrl("https://res.cloudinary.com/knight-cloud/image/upload/v1586525177/wkgo2xepwqooozuf5lha.png");
        photo.setIdInCloud("wkgo2xepwqooozuf5lha");

        photoRepository.save(photo);

        Category category = new Category();
        category.setName("Vehicles");
        category.setPhoto(photo);

        categoryRepository.save(category);
    }


    @Override
    public AdvertisementServiceModel getById(Long id) {
        return advertisementRepository.findById(id)
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .orElseThrow();
    }

    @Override
    public Long countOfAll() {
        return advertisementRepository.count();
    }

    @Override
    public Long countByCategoryAndPriceBetween(String category, BigDecimal min, BigDecimal max) {

        Long result;

        if (categoryRepository.existsByName(category)) {
            result = advertisementRepository.countAdvertisementByCategoryNameAndPriceBetween(category, min, max);
        } else {
            result = 0L;
        }

        return result;
    }

    @Override
    public Long countOfPriceBetween(BigDecimal min, BigDecimal max) {
        return advertisementRepository.countAdvertisementByPriceBetween(min, max);
    }

    @Override
    public Long countOfPriceBetweenAndCondition(BigDecimal min, BigDecimal max, String conditionName) {
        Condition condition = Condition.valueOf(conditionName);
        return advertisementRepository.countAdvertisementByPriceBetweenAndCondition(min, max, condition);
    }

    @Override
    public Long countByCategoryConditionAndPriceBetween(String category, String conditionName, BigDecimal min, BigDecimal max) {

        Long count;

        if (categoryRepository.existsByName(category) &&
                Arrays.stream(Condition.values()).anyMatch(x -> x.name().equals(conditionName.toUpperCase()))) {

            Condition condition = Condition.valueOf(conditionName);
            count = advertisementRepository.countAdvertisementByCategoryNameAndConditionAndPriceBetween(category, condition, min, max);

        } else {
            count = 0L;
        }

        return count;
    }

    @Override
    public ResponseMessage create(AdvertisementCreateModel restModel) {

        AdvertisementServiceModel advertisement = modelMapper.map(restModel, AdvertisementServiceModel.class);
        advertisement.setViews(0L);

        if (!validationService.isValid(advertisement)) {
            return new ResponseMessage(FAIL);
        }

        Category category = categoryRepository.findById(restModel.getCategory()).orElse(null);

        if (category == null) {
            return new ResponseMessage(FAIL);
        }

        UserProfile userProfile = userProfileRepository.findByUserUsername(restModel.getCreator()).orElse(null);

        if (userProfile == null) {
            return new ResponseMessage(FAIL);
        }

        Advertisement entity = modelMapper.map(advertisement, Advertisement.class);
        entity.setCategory(category);
        entity.setCreator(userProfile);
        entity.setPhotos(Arrays.stream(restModel.getImages()).map(photoService::create)
                .map(x -> modelMapper.map(x, Photo.class))
                .collect(Collectors.toList()));

        advertisementRepository.save(entity);

        return new ResponseMessage(SUCCESS);
    }

    @Override
    public ResponseMessage edit(AdvertisementEditedModel restModel) {

        Advertisement advertisement = advertisementRepository.findById(restModel.getId()).orElse(null);

        Category category = categoryRepository.findById(restModel.getCategory()).orElse(null);

        if (advertisement == null
                || !advertisement.getCreator().getUser().getUsername().equals(restModel.getEditor())
                || category == null) {
            return new ResponseMessage(FAIL);
        }

        if (!advertisement.getCategory().getId().equals(category.getId())) {
            advertisement.setCategory(category);
        }

        advertisement.setTitle(restModel.getTitle());
        advertisement.setDescription(restModel.getDescription());
        advertisement.setPrice(restModel.getPrice());
        advertisement.setCondition(Condition.valueOf(restModel.getCondition()));

        advertisementRepository.save(advertisement);
        return new ResponseMessage(SUCCESS);
    }

    @Override
    public ResponseMessage delete(String principalName, String requestSender, Long id) {

        Advertisement advertisement = advertisementRepository.findById(id).orElse(null);

        if (advertisement == null || !principalName.equals(requestSender)
                || !advertisement.getCreator().getUser().getUsername().equals(principalName)) {
            return new ResponseMessage(FAIL);
        }

        advertisement.getCreator().getCreatedAdvertisements().remove(advertisement);
        advertisement.getProfilesWhichLikedIt().forEach(p -> p.getFavorites().remove(advertisement));
        photoService.deleteAll(advertisement.getPhotos().stream().map(Photo::getId).collect(Collectors.toList()));

        advertisementRepository.save(advertisement);
        advertisementRepository.delete(advertisement);

        return new ResponseMessage(SUCCESS);
    }

    @Override
    public ResponseMessage increaseViews(Long id, Long updatedViews) {

        Advertisement advertisement = advertisementRepository.findById(id).orElse(null);

        if (advertisement == null) {
            return new ResponseMessage(FAIL);
        }

        advertisement.setViews(updatedViews);

        advertisementRepository.save(advertisement);

        return new ResponseMessage(SUCCESS);
    }

    @Override
    public ResponseMessage detachPhoto(String username, Long id, Long photoId) {

        Advertisement advertisement = advertisementRepository.findById(id).orElse(null);

        if (advertisement == null || advertisement.getPhotos().stream().noneMatch(x -> x.getId().equals(photoId)) ||
                !advertisement.getCreator().getUser().getUsername().equals(username)) {
            return new ResponseMessage(FAIL);
        }

        advertisement.getPhotos().remove(advertisement.getPhotos().stream().filter(x -> x.getId().equals(photoId)).findFirst().get());

        advertisementRepository.save(advertisement);

        return new ResponseMessage(SUCCESS);
    }

    @Override
    public List<AdvertisementServiceModel> getAllByCategoryTitleContainingAndPriceBetween(String categoryName, String searchText, BigDecimal min, BigDecimal max, PageRequest page) {

        Page<Advertisement> advertisements;

        if (categoryName.equals("All")) {
            advertisements = advertisementRepository.findAllByPriceBetweenAndTitleContaining(min, max, searchText, page);
        } else {
            advertisements = advertisementRepository.findAllByCategoryNameAndTitleContainingAndPriceBetween(categoryName, searchText, min, max, page);
        }

        return advertisements
                .stream()
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AdvertisementServiceModel> getAllByCategoryPriceBetweenAndCondition(BigDecimal min, BigDecimal max, String conditionName, String category, PageRequest pageRequest) {

        Condition condition = Condition.valueOf(conditionName);

        Page<Advertisement> advertisements;

        if (category.equals("All")) {
            advertisements = advertisementRepository.findAllByPriceBetweenAndCondition(min, max, condition, pageRequest);
        } else {
            advertisements = advertisementRepository.findAllByCategoryNameAndPriceBetweenAndCondition(category, min, max, condition, pageRequest);
        }

        return advertisements
                .stream()
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AdvertisementServiceModel> getAllByCategoryAndPriceBetween(String category, BigDecimal min, BigDecimal max, PageRequest pageRequest) {

        this.seedCategories();

        Page<Advertisement> advertisements;

        if (category.equals("All")) {
            advertisements = advertisementRepository.findAllByPriceBetween(min, max, pageRequest);
        } else {
            advertisements = advertisementRepository.findAllByCategoryNameAndPriceBetween(category, min, max, pageRequest);
        }

        return advertisements
                .stream()
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AdvertisementServiceModel> getAllByCategoryTitleContainingPriceBetweenAndCondition(String category, String search, BigDecimal min, BigDecimal max, String conditionName, PageRequest pageRequest) {

        Page<Advertisement> advertisements;

        Condition condition = Condition.valueOf(conditionName);

        if (category.equals("All")) {
            advertisements = advertisementRepository.findAllByTitleContainingAndPriceBetweenAndCondition(search, min, max, condition, pageRequest);
        } else {
            advertisements = advertisementRepository.findAllByTitleContainingAndCategoryNameAndPriceBetweenAndCondition(search, category, min, max, condition, pageRequest);
        }

        return advertisements
                .stream()
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long countByCategoryTitleContainingPriceBetweenAndCondition(String category, String search, BigDecimal min, BigDecimal max, String conditionName) {

        Long count;

        if (category.equals("All")) {

            if (conditionName.equals("All")) {
                count = advertisementRepository.countByPriceBetweenAndTitleContaining(min, max, search);
            } else {
                Condition condition = Condition.valueOf(conditionName);
                count = advertisementRepository.countByPriceBetweenAndTitleContainingAndCondition(min, max, search, condition);
            }

        } else {

            if (conditionName.equals("All")) {

                count = advertisementRepository.countByPriceBetweenAndTitleContainingAndCategoryName(min, max, search, category);
            } else {
                Condition condition = Condition.valueOf(conditionName);
                count = advertisementRepository.countByPriceBetweenAndTitleContainingAndCategoryNameAndCondition(min,max,search,category,condition);
            }

        }

        return count;
    }
}
