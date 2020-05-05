package TradeZone.service;

import TradeZone.data.error.exception.AdvertisementNotValidException;
import TradeZone.data.error.exception.EntityNotFoundException;
import TradeZone.data.error.exception.SearchNotValidException;
import TradeZone.data.error.exception.NotAllowedException;
import TradeZone.data.model.rest.*;
import TradeZone.data.model.rest.search.*;
import TradeZone.data.model.service.validation.FullSearchRequestValidationService;
import TradeZone.data.model.service.validation.SearchRequestValidationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import TradeZone.data.model.entity.Advertisement;
import TradeZone.data.model.entity.Category;
import TradeZone.data.model.entity.Photo;
import TradeZone.data.model.entity.UserProfile;
import TradeZone.data.model.enums.Condition;
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

    private static final String NOT_ALLOWED = "You are not allowed to perform this action";
    private static final String INVALID_MODEL = "Invalid data passed";
    private static final String INVALID_SEARCH = "Invalid search data";
    private static final String ADV_NOT_FOUND = "Advertisement with id %d not found";
    private static final String CAT_NOT_FOUND = "Category with id %d not found";
    private static final String PROFILE_NOT_FOUND = "Profile with username %s not found";
    private static final String IMAGE_NOT_FOUND = "Photo with id %d not found";
    private static final String UNDEFINED = "undefined";
    private static final String DEFAULT = "All";

    private final AdvertisementRepository advertisementRepository;

    private final AdvertisementValidationService validationService;

    private final FullSearchRequestValidationService fullSearchValidationService;

    private final SearchRequestValidationService searchValidationService;

    private final PhotoService photoService;

    private final ModelMapper modelMapper;

    private final UserProfileRepository userProfileRepository;

    private final CategoryRepository categoryRepository;

    private final PhotoRepository photoRepository;

    @Override
    public AdvertisementServiceModel getById(Long id) {
        return advertisementRepository.findById(id)
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADV_NOT_FOUND, id)));
    }

    @Override
    public void create(AdvertisementCreateModel restModel) {

        if (!validationService.isValid(restModel)) {
            throw new AdvertisementNotValidException(INVALID_MODEL);
        }

        Category category = categoryRepository.findById(restModel.getCategory())
                .orElseThrow(() -> new EntityNotFoundException(String.format(CAT_NOT_FOUND, restModel.getCategory())));

        UserProfile userProfile = userProfileRepository.findByUserUsername(restModel.getCreator())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, restModel.getCreator())));

        Advertisement entity = modelMapper.map(restModel, Advertisement.class);
        entity.setViews(0L);
        entity.setCategory(category);
        entity.setCreator(userProfile);
        entity.setPhotos(Arrays.stream(restModel.getImages()).map(photoService::create)
                .map(x -> modelMapper.map(x, Photo.class))
                .collect(Collectors.toList()));

        advertisementRepository.save(entity);
    }

    @Override
    public void edit(AdvertisementEditedModel restModel) {

        if (!validationService.isValid(restModel)) {
            throw new AdvertisementNotValidException(INVALID_MODEL);
        }

        Advertisement advertisement = advertisementRepository.findById(restModel.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADV_NOT_FOUND, restModel.getId())));

        Category category = categoryRepository.findById(restModel.getCategory())
                .orElseThrow(() -> new EntityNotFoundException(String.format(CAT_NOT_FOUND, restModel.getCategory())));

        if (!advertisement.getCreator().getUser().getUsername().equals(restModel.getCreator())) {
            throw new NotAllowedException(NOT_ALLOWED);
        }

        if (!advertisement.getCategory().getId().equals(category.getId())) {
            advertisement.setCategory(category);
        }

        advertisement.setTitle(restModel.getTitle());
        advertisement.setDescription(restModel.getDescription());
        advertisement.setPrice(restModel.getPrice());
        advertisement.setCondition(Condition.valueOf(restModel.getCondition()));

        advertisementRepository.save(advertisement);
    }

    @Override
    public void delete(String principalName, DeleteAdvRequest deleteRequest) {

        Advertisement advertisement = advertisementRepository.findById(deleteRequest.getAdvertisementId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADV_NOT_FOUND, deleteRequest.getAdvertisementId())));

        if (!principalName.equals(deleteRequest.getUsername())
                || !advertisement.getCreator().getUser().getUsername().equals(principalName)) {

            throw new NotAllowedException(NOT_ALLOWED);
        }

        advertisement.getCreator().getCreatedAdvertisements().remove(advertisement);
        advertisement.getProfilesWhichLikedIt().forEach(p -> p.getFavorites().remove(advertisement));
        advertisement.getProfilesWhichViewedIt().forEach(p -> p.getViewed().remove(advertisement));

        photoService.deleteAll(advertisement.getPhotos().stream().map(Photo::getId).collect(Collectors.toList()));

        advertisementRepository.save(advertisement);
        advertisementRepository.delete(advertisement);
    }

    @Override
    public void updateViews(Long id, ViewsUpdate update) {

        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADV_NOT_FOUND));

        UserProfile profile = userProfileRepository.findByUserUsername(update.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, update.getUsername())));

        if (profile.getViewed().stream().anyMatch(x -> x.getId().equals(id))) {
            return;
        }

        advertisement.setViews(update.getViews());
        advertisementRepository.save(advertisement);

        profile.getViewed().add(advertisement);
        userProfileRepository.save(profile);
    }

    @Override
    public void deletePhoto(DeleteAdvImageRequest request) {

        Advertisement advertisement = advertisementRepository.findById(request.getAdvertisementId())
                .orElseThrow(() -> new EntityNotFoundException(ADV_NOT_FOUND));

        if (advertisement.getPhotos().stream().noneMatch(x -> x.getId().equals(request.getPhotoId())) ||
                !advertisement.getCreator().getUser().getUsername().equals(request.getUsername())) {

            throw new EntityNotFoundException(IMAGE_NOT_FOUND);
        }

        advertisement.getPhotos().remove(advertisement.getPhotos().stream()
                .filter(x -> x.getId().equals(request.getPhotoId())).findFirst().get());

        advertisementRepository.save(advertisement);
    }

    @Override
    public Long getCountBySearch(SearchRequest searchRequest) {

        if (!searchValidationService.isValid(searchRequest)) {
            throw new SearchNotValidException(INVALID_SEARCH);
        }

        String category = searchRequest.getCategory();
        String conditionName = searchRequest.getCondition();
        String search = searchRequest.getSearch();
        BigDecimal min = searchRequest.getMin();
        BigDecimal max = searchRequest.getMax();

        Long count;

        if (category.equals(DEFAULT)) {
            if (conditionName.equals(DEFAULT)) {
                if (search.equals(UNDEFINED)) {
                    count = advertisementRepository.countAdvertisementByPriceBetween(min, max);
                } else {
                    count = advertisementRepository.countByPriceBetweenAndTitleContaining(min, max, search);
                }
            } else {
                Condition condition = Condition.valueOf(conditionName);
                if (search.equals(UNDEFINED)) {
                    count = advertisementRepository.countAdvertisementByPriceBetweenAndCondition(min, max, condition);
                } else {
                    count = advertisementRepository.countByPriceBetweenAndTitleContainingAndCondition(min, max, search, condition);
                }
            }
        } else {
            if (conditionName.equals(DEFAULT)) {
                if (search.equals(UNDEFINED)) {
                    count = advertisementRepository.countAdvertisementByCategoryNameAndPriceBetween(category, min, max);
                } else {
                    count = advertisementRepository.countByPriceBetweenAndTitleContainingAndCategoryName(min, max, search, category);
                }
            } else {
                Condition condition = Condition.valueOf(conditionName);
                if (search.equals(UNDEFINED)) {
                    count = advertisementRepository.countAdvertisementByCategoryNameAndConditionAndPriceBetween(category, condition, min, max);
                } else {
                    count = advertisementRepository.countByPriceBetweenAndTitleContainingAndCategoryNameAndCondition(min, max, search, category, condition);
                }
            }
        }

        return count;
    }

    @Override
    public Page<AdvertisementServiceModel> getAllByFullSearch(FullSearchRequest request) {

        if (!fullSearchValidationService.isValid(request)) {
            throw new SearchNotValidException(INVALID_SEARCH);
        }

        Integer page = request.getPage();
        String sortBy = request.getSortBy();
        String order = request.getOrder();
        String condition = request.getCondition();
        PageRequest pageRequest = buildPageRequest(page, sortBy, order);

        Page<AdvertisementServiceModel> advertisements;

        if (request.getSearch().equals(UNDEFINED)) {

            BigDecimal min = request.getMin();
            BigDecimal max = request.getMax();
            String category = request.getCategory();

            if (!condition.equals(DEFAULT)) {
                advertisements = getAllByCategoryPriceBetweenAndCondition(request, pageRequest);
            } else {
                advertisements = getAllByCategoryAndPriceBetween(category, min, max, pageRequest);
            }

        } else {

            if (!condition.equals(DEFAULT)) {
                advertisements = getAllByCategoryTitleContainingPriceBetweenAndCondition(request, pageRequest);
            } else {
                advertisements = getAllByCategoryTitleContainingAndPriceBetween(request, pageRequest);
            }
        }

        return advertisements;
    }

    private Page<AdvertisementServiceModel> getAllByCategoryTitleContainingPriceBetweenAndCondition(SearchRequest searchRequest, PageRequest pageRequest) {

        String conditionName = searchRequest.getCondition();
        String category = searchRequest.getCategory();
        String search = searchRequest.getSearch();
        BigDecimal min = searchRequest.getMin();
        BigDecimal max = searchRequest.getMax();


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

    private Page<AdvertisementServiceModel> getAllByCategoryPriceBetweenAndCondition(FullSearchRequest searchRequest, PageRequest pageRequest) {

        String conditionName = searchRequest.getCondition();
        String category = searchRequest.getCategory();
        BigDecimal min = searchRequest.getMin();
        BigDecimal max = searchRequest.getMax();


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

    private Page<AdvertisementServiceModel> getAllByCategoryTitleContainingAndPriceBetween(SearchRequest request, PageRequest page) {

        String categoryName = request.getCategory();
        BigDecimal min = request.getMin();
        BigDecimal max = request.getMax();
        String searchText = request.getSearch();

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

    private PageRequest buildPageRequest(Integer pageNumber, String sortBy, String order) {

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 6);

        if (!sortBy.equals("none") && !order.equals("none")) {

            Sort sort = Sort.by(sortBy);
            if (order.equals("ascending")) {
                sort = sort.ascending();
            } else {
                sort = sort.descending();
            }
            pageRequest = PageRequest.of(pageNumber - 1, 6, sort);
        }

        return pageRequest;
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
