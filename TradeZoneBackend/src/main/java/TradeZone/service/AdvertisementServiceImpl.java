package TradeZone.service;

import TradeZone.data.error.exception.*;
import TradeZone.data.model.enums.DeliveryType;
import TradeZone.data.model.rest.*;
import TradeZone.data.model.rest.search.*;
import TradeZone.service.validation.*;
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
import TradeZone.data.repository.AdvertisementRepository;
import TradeZone.data.repository.CategoryRepository;
import TradeZone.data.repository.UserProfileRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
    private static final String ADV_IMG_NOT_PRESENT = "Photo not belong to the advertisement";
    private static final String INVALID_DELETE_REQUEST = "Invalid delete request";
    private static final String INVALID_VIEWS_UPDATE = "Invalid views update";
    private static final String PROFILE_NOT_COMPLETED = "Cant do because profile not completed";
    private static final String UNDEFINED = "undefined";
    private static final String NONE_SORT = "none";
    private static final String ASCENDING_ORDER = "ascending";
    private static final String DEFAULT = "All";

    private final AdvertisementRepository advertisementRepository;

    private final AdvertisementValidationService validationService;

    private final FullSearchRequestValidationService fullSearchValidationService;

    private final SearchRequestValidationService searchValidationService;

    private final DeleteAdvRequestValidationService deleteAdvRequestValidationService;

    private final DeleteAdvImageRequestValidationService deleteAdvImageRequestValidationService;

    private final ViewsUpdateValidationService viewsUpdateValidationService;

    private final PhotoService photoService;

    private final ModelMapper modelMapper;

    private final UserProfileRepository userProfileRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public AdvertisementServiceModel getById(Long id) {
        return advertisementRepository.findById(id)
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADV_NOT_FOUND, id)));
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
    public AdvertisementServiceModel create(AdvertisementCreateModel restModel) {

        if (!validationService.isValid(restModel)) {
            throw new AdvertisementNotValidException(INVALID_MODEL);
        }

        Category category = categoryRepository.findById(restModel.getCategory())
                .orElseThrow(() -> new EntityNotFoundException(String.format(CAT_NOT_FOUND, restModel.getCategory())));

        UserProfile userProfile = userProfileRepository.findByUserUsername(restModel.getCreator())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, restModel.getCreator())));

        if (!userProfile.getIsCompleted()) {
            throw new ProfileNotCompletedException(PROFILE_NOT_COMPLETED);
        }

        Advertisement entity = modelMapper.map(restModel, Advertisement.class);
        entity.setViews(0L);
        entity.setCreatedOn(LocalDateTime.now());
        entity.setCategory(category);
        entity.setCreator(userProfile);
        entity.setPhotos(Arrays.stream(restModel.getImages()).map(photoService::upload)
                .map(x -> modelMapper.map(x, Photo.class))
                .collect(Collectors.toList()));

        entity = advertisementRepository.save(entity);

        return modelMapper.map(entity, AdvertisementServiceModel.class);
    }

    @Override
    public AdvertisementServiceModel edit(AdvertisementEditedModel restModel) {

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
        advertisement.setDelivery(DeliveryType.valueOf(restModel.getDelivery()));

        advertisement = advertisementRepository.save(advertisement);

        return modelMapper.map(advertisement, AdvertisementServiceModel.class);
    }

    @Override
    public AdvertisementServiceModel delete(String principalName, DeleteAdvRequest deleteRequest) {

        if (!deleteAdvRequestValidationService.isValid(deleteRequest)) {
            throw new DeleteRequestNotValidException(INVALID_DELETE_REQUEST);
        }

        Advertisement advertisement = advertisementRepository.findById(deleteRequest.getAdvertisementId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADV_NOT_FOUND, deleteRequest.getAdvertisementId())));

        if (!principalName.equals(deleteRequest.getUsername())
                || !advertisement.getCreator().getUser().getUsername().equals(principalName)) {

            throw new NotAllowedException(NOT_ALLOWED);
        }

        advertisement.getCreator().getCreatedAdvertisements().remove(advertisement);
        advertisement.getProfilesWhichLikedIt().forEach(p -> p.getFavorites().remove(advertisement));
        advertisement.getProfilesWhichViewedIt().forEach(p -> p.getViewed().remove(advertisement));

        advertisement.setProfilesWhichLikedIt(null);
        advertisement.setProfilesWhichViewedIt(null);

        photoService.deleteAll(advertisement.getPhotos().stream().map(Photo::getId).collect(Collectors.toList()));

        advertisementRepository.save(advertisement);
        advertisementRepository.delete(advertisement);

        return modelMapper.map(advertisement, AdvertisementServiceModel.class);
    }

    @Override
    public AdvertisementServiceModel updateViews(Long id, ViewsUpdate update) {

        if (!viewsUpdateValidationService.isValid(update)) {
            throw new ViewsUpdateNotValidException(INVALID_VIEWS_UPDATE);
        }

        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADV_NOT_FOUND));

        UserProfile profile = userProfileRepository.findByUserUsername(update.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, update.getUsername())));

        if (profile.getViewed().stream().noneMatch(x -> x.getId().equals(id))) {

            advertisement.setViews(update.getViews());
            advertisementRepository.save(advertisement);

            profile.getViewed().add(advertisement);
            userProfileRepository.save(profile);
        }

        return modelMapper.map(advertisement, AdvertisementServiceModel.class);
    }

    @Override
    public AdvertisementServiceModel deletePhoto(DeleteAdvImageRequest request) {

        if (!deleteAdvImageRequestValidationService.isValid(request)) {
            throw new DeleteRequestNotValidException(INVALID_DELETE_REQUEST);
        }

        Advertisement advertisement = advertisementRepository.findById(request.getAdvertisementId())
                .orElseThrow(() -> new EntityNotFoundException(ADV_NOT_FOUND));

        if (advertisement.getPhotos().stream().noneMatch(x -> x.getId().equals(request.getPhotoId()))) {
            throw new EntityNotFoundException(ADV_IMG_NOT_PRESENT);
        }

        if (!advertisement.getCreator().getUser().getUsername().equals(request.getUsername())) {
            throw new NotAllowedException(NOT_ALLOWED);
        }

        advertisement.getPhotos().remove(advertisement.getPhotos().stream()
                .filter(x -> x.getId().equals(request.getPhotoId())).findFirst().get());

        photoService.delete(request.getPhotoId());

        advertisementRepository.save(advertisement);

        return modelMapper.map(advertisement, AdvertisementServiceModel.class);
    }

    //not tested
    @Override
    public List<AdvertisementServiceModel> findByCreatorUsernameExcept(SpecificSearch specificSearch, String username) {

        PageRequest pageRequest = PageRequest.of(specificSearch.getPage(), 3, Sort.by("views").descending());

        return advertisementRepository.findAllByCreatorUserUsernameAndIdNot(username, pageRequest, specificSearch.getExcludeId())
                .stream()
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .collect(Collectors.toList());
    }

    private Page<AdvertisementServiceModel> getAllByCategoryTitleContainingPriceBetweenAndCondition(SearchRequest searchRequest, PageRequest pageRequest) {

        String conditionName = searchRequest.getCondition();
        String category = searchRequest.getCategory();
        String search = searchRequest.getSearch();
        BigDecimal min = searchRequest.getMin();
        BigDecimal max = searchRequest.getMax();

        Page<Advertisement> advertisements;

        Condition condition = Condition.valueOf(conditionName);

        if (category.equals(DEFAULT)) {
            advertisements = advertisementRepository.findAllByTitleContainingAndPriceBetweenAndCondition(search, min, max, condition, pageRequest);
        } else {
            advertisements = advertisementRepository.findAllByTitleContainingAndCategoryNameAndPriceBetweenAndCondition(search, category, min, max, condition, pageRequest);
        }

        return new PageImpl<>(advertisements.stream()
                .map(x -> modelMapper.map(x, AdvertisementServiceModel.class))
                .collect(Collectors.toList()));
    }

    private Page<AdvertisementServiceModel> getAllByCategoryAndPriceBetween(String category, BigDecimal min, BigDecimal max, PageRequest pageRequest) {

        Page<Advertisement> advertisements;

        if (category.equals(DEFAULT)) {
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

        if (category.equals(DEFAULT)) {
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

        if (categoryName.equals(DEFAULT)) {
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

        if (!sortBy.equals(NONE_SORT) && !order.equals(NONE_SORT)) {

            Sort sort = Sort.by(sortBy);
            if (order.equals(ASCENDING_ORDER)) {
                sort = sort.ascending();
            } else {
                sort = sort.descending();
            }
            pageRequest = PageRequest.of(pageNumber - 1, 6, sort);
        }

        return pageRequest;
    }
}
