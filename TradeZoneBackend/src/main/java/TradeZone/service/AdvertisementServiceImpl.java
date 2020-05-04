package TradeZone.service;

import TradeZone.data.error.exception.EntityNotFoundException;
import TradeZone.data.model.rest.*;
import TradeZone.data.model.rest.search.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import TradeZone.data.model.entity.Advertisement;
import TradeZone.data.model.entity.Category;
import TradeZone.data.model.entity.Photo;
import TradeZone.data.model.entity.UserProfile;
import TradeZone.data.model.enums.Condition;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.service.validation.AdvertisementValidationService;
import TradeZone.data.repository.AdvertisementRepository;
import TradeZone.data.repository.CategoryRepository;
import TradeZone.data.repository.PhotoRepository;
import TradeZone.data.repository.UserProfileRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private static final String FAIL = "FAIL";

    private static final String ADV_NOT_FOUND = "Advertisement with id %d not found";

    private static final String PROFILE_NOT_FOUND = "Profile with username %s not found";

    private static final String IMAGE_NOT_FOUND = "Photo with id %d not found";

    private static final String SUCCESS = "SUCCESS";

    private final AdvertisementRepository advertisementRepository;

    private final AdvertisementValidationService validationService;

    private final PhotoService photoService;

    private final ModelMapper modelMapper;

    private final UserProfileRepository userProfileRepository;

    private final CategoryRepository categoryRepository;

    private final PhotoRepository photoRepository;

    @Override
    public AdvertisementServiceModel getById(Long id) throws EntityNotFoundException {
        return advertisementRepository.findById(id)
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADV_NOT_FOUND, id)));
    }

    @Override
    public Long countOfPriceBetween(BaseSearch baseSearch) {
        return advertisementRepository.countAdvertisementByPriceBetween(baseSearch.getMin(), baseSearch.getMax());
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
    public void updateViews(Long id, ViewsUpdate update) throws EntityNotFoundException {

        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADV_NOT_FOUND));

        UserProfile profile = userProfileRepository.findByUserUsername(update.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, update.getUsername())));

        if (profile.getViewed().stream().anyMatch(x -> x.getId().equals(id))){
            return;
        }

        advertisement.setViews(update.getViews());
        advertisementRepository.save(advertisement);

        profile.getViewed().add(advertisement);
        userProfileRepository.save(profile);
    }

    @Override
    public void detachPhoto(String username, Long id, Long photoId) throws EntityNotFoundException {

        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADV_NOT_FOUND));

        if (advertisement.getPhotos().stream().noneMatch(x -> x.getId().equals(photoId)) ||
                !advertisement.getCreator().getUser().getUsername().equals(username)) {

            throw new EntityNotFoundException(IMAGE_NOT_FOUND);
        }

        advertisement.getPhotos().remove(advertisement.getPhotos().stream()
                .filter(x -> x.getId().equals(photoId)).findFirst().get());

        advertisementRepository.save(advertisement);
    }

    @Override
    public Long countByCategoryTitleContainingPriceBetweenAndCondition(SearchRequest searchRequest) {

        String category = searchRequest.getCategory();
        String conditionName = searchRequest.getCondition();
        String search = searchRequest.getSearch();
        BigDecimal min = searchRequest.getMin();
        BigDecimal max = searchRequest.getMax();

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
                count = advertisementRepository.countByPriceBetweenAndTitleContainingAndCategoryNameAndCondition(min, max, search, category, condition);
            }

        }

        return count;
    }

    @Override
    public Long countByPriceBetweenAndCondition(ConditionSearch search) {

        String condition = search.getCondition();
        BigDecimal min = search.getMin();
        BigDecimal max = search.getMax();

        Long count;

        if (!condition.equals("All")) {
            count = countOfPriceBetweenAndCondition(min, max, condition);
        } else {
            count = countOfPriceBetween(search);
        }

        return count;
    }

    @Override
    public Long countByCategory(CategorySearchRequest search) {

        String condition = search.getCondition();
        String category = search.getCategory();
        BigDecimal min = search.getMin();
        BigDecimal max = search.getMax();

        Long count;


        if (condition.equals("All")) {
            count = countByCategoryAndPriceBetween(category, min, max);
        } else {
            count = countByCategoryConditionAndPriceBetween(category, condition, min, max);
        }

        return count;
    }

    @Override
    public Page<AdvertisementServiceModel> getAllByFullSearch(FullSearchRequest request) {

        Integer page = request.getPage();
        String sortBy = request.getSortBy();
        String order = request.getOrder();
        String condition = request.getCondition();
        String category = request.getCategory();
        BigDecimal min = request.getMin();
        BigDecimal max = request.getMax();
        String search = request.getSearch();

        PageRequest pageRequest = PageRequest.of(page - 1, 6);

        if (!sortBy.equals("none") && !order.equals("none")) {

            Sort sort = Sort.by(sortBy);
            if (order.equals("ascending")) {
                sort = sort.ascending();
            } else {
                sort = sort.descending();
            }
            pageRequest = PageRequest.of(page - 1, 6, sort);
        }

        Page<AdvertisementServiceModel> advertisements;

        if (!condition.equals("All")) {
            advertisements = getAllByCategoryTitleContainingPriceBetweenAndCondition(category, search, min, max, condition, pageRequest);
        } else {
            advertisements = getAllByCategoryTitleContainingAndPriceBetween(category, search, min, max, pageRequest);
        }

        return advertisements;
    }

    @Override
    public Page<AdvertisementServiceModel> getAllByAlmostFullSearch(AlmostFullSearchRequest searchRequest) {

        PageRequest pageRequest = PageRequest.of(searchRequest.getPage() - 1, 6);

        String sortBy = searchRequest.getSortBy();
        String order = searchRequest.getOrder();
        Integer page = searchRequest.getPage();
        String condition = searchRequest.getCondition();
        BigDecimal min = searchRequest.getMin();
        BigDecimal max = searchRequest.getMax();
        String category = searchRequest.getCategory();

        if (!sortBy.equals("none") && !order.equals("none")) {

            Sort sort = Sort.by(sortBy);
            if (order.equals("ascending")) {
                sort = sort.ascending();
            } else {
                sort = sort.descending();
            }
            pageRequest = PageRequest.of(page - 1, 6, sort);
        }

        Page<AdvertisementServiceModel> advertisementServiceModels;

        if (!condition.equals("All")) {
            advertisementServiceModels = getAllByCategoryPriceBetweenAndCondition(min, max, condition, category, pageRequest);
        } else {
            advertisementServiceModels = getAllByCategoryAndPriceBetween(category, min, max, pageRequest);
        }

        return advertisementServiceModels;
    }

    private Page<AdvertisementServiceModel> getAllByCategoryTitleContainingPriceBetweenAndCondition(String category, String search, BigDecimal min, BigDecimal max, String conditionName, PageRequest pageRequest) {

        Page<Advertisement> advertisements;

        Condition condition = Condition.valueOf(conditionName);

        if (category.equals("All")) {
            advertisements = advertisementRepository.findAllByTitleContainingAndPriceBetweenAndCondition(search, min, max, condition, pageRequest);
        } else {
            advertisements = advertisementRepository.findAllByTitleContainingAndCategoryNameAndPriceBetweenAndCondition(search, category, min, max, condition, pageRequest);
        }

        return new PageImpl<>(advertisements.stream()
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .collect(Collectors.toList()));
    }

    private Page<AdvertisementServiceModel> getAllByCategoryAndPriceBetween(String category, BigDecimal min, BigDecimal max, PageRequest pageRequest) {

        this.seedCategories();

        Page<Advertisement> advertisements;

        if (category.equals("All")) {
            advertisements = advertisementRepository.findAllByPriceBetween(min, max, pageRequest);
        } else {
            advertisements = advertisementRepository.findAllByCategoryNameAndPriceBetween(category, min, max, pageRequest);
        }

        return new PageImpl<>(advertisements.stream()
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .collect(Collectors.toList()));
    }

    private Page<AdvertisementServiceModel> getAllByCategoryPriceBetweenAndCondition(BigDecimal min, BigDecimal max, String conditionName, String category, PageRequest pageRequest) {

        Condition condition = Condition.valueOf(conditionName);

        Page<Advertisement> advertisements;

        if (category.equals("All")) {
            advertisements = advertisementRepository.findAllByPriceBetweenAndCondition(min, max, condition, pageRequest);
        } else {
            advertisements = advertisementRepository.findAllByCategoryNameAndPriceBetweenAndCondition(category, min, max, condition, pageRequest);
        }

        return new PageImpl<>(advertisements.stream()
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .collect(Collectors.toList()));
    }

    private Page<AdvertisementServiceModel> getAllByCategoryTitleContainingAndPriceBetween(String categoryName, String searchText, BigDecimal min, BigDecimal max, PageRequest page) {

        Page<Advertisement> advertisements;

        if (categoryName.equals("All")) {
            advertisements = advertisementRepository.findAllByPriceBetweenAndTitleContaining(min, max, searchText, page);
        } else {
            advertisements = advertisementRepository.findAllByCategoryNameAndTitleContainingAndPriceBetween(categoryName, searchText, min, max, page);
        }

        return new PageImpl<>(advertisements.stream()
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .collect(Collectors.toList()));
    }

    private Long countByCategoryConditionAndPriceBetween(String category, String conditionName, BigDecimal min, BigDecimal max) {

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

    private Long countOfPriceBetweenAndCondition(BigDecimal min, BigDecimal max, String conditionName) {
        Condition condition = Condition.valueOf(conditionName);
        return advertisementRepository.countAdvertisementByPriceBetweenAndCondition(min, max, condition);
    }

    private Long countByCategoryAndPriceBetween(String category, BigDecimal min, BigDecimal max) {

        Long result;

        if (categoryRepository.existsByName(category)) {
            result = advertisementRepository.countAdvertisementByCategoryNameAndPriceBetween(category, min, max);
        } else {
            result = 0L;
        }

        return result;
    }

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
}
