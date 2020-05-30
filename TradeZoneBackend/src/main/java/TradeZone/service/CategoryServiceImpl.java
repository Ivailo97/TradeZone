package TradeZone.service;

import TradeZone.data.error.exception.CategoryNotValidException;
import TradeZone.data.model.entity.Photo;
import TradeZone.data.model.rest.CategoryCreateModel;
import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.service.PhotoServiceModel;
import TradeZone.data.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import TradeZone.data.model.entity.Category;
import TradeZone.data.model.entity.UserProfile;
import TradeZone.data.model.service.CategoryServiceModel;
import TradeZone.service.validation.CategoryValidationService;
import TradeZone.data.repository.CategoryRepository;
import TradeZone.data.repository.UserProfileRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final String INVALID_MODEL = "FAIL -> INVALID DATA";
    private static final String DEFAULT_CATEGORY = "Vehicles";
    private static final String DEFAULT_CATEGORY_ID_CLOUD = "wkgo2xepwqooozuf5lha";
    private static final String DEFAULT_CATEGORY_PHOTO_CLOUD_URL = "https://res.cloudinary.com/knight-cloud/image/upload/v1586525177/wkgo2xepwqooozuf5lha.png";

    private final CategoryRepository repository;

    private final UserProfileRepository profileRepository;

    private final PhotoRepository photoRepository;

    private final PhotoService photoService;

    private final CategoryValidationService validationService;

    private final ModelMapper mapper;

    @Override
    @Transactional
    public CategoryServiceModel create(CategoryCreateModel restModel) {

        CategoryServiceModel category = mapper.map(restModel, CategoryServiceModel.class);
        PhotoServiceModel photo = photoService.upload(restModel.getImage());
        category.setPhoto(photo);

        if (!validationService.isValid(category)) {
            throw new CategoryNotValidException(INVALID_MODEL);
        }

        UserProfile userProfile = profileRepository.findByUserUsername(restModel.getCreator()).orElse(null);

        if (userProfile == null) {
            throw new CategoryNotValidException(INVALID_MODEL);
        }

        Category entity = mapper.map(category, Category.class);
        entity.setCreator(userProfile);
        entity = repository.save(entity);

        return mapper.map(entity, CategoryServiceModel.class);
    }

    @Override
    public List<CategoryServiceModel> getTop(Integer count) {
        return this.repository.findTopOrderedByAdvertisementsCountDesc(count)
                .stream().map(x -> {
                    CategoryServiceModel serviceModel = this.mapper.map(x, CategoryServiceModel.class);
                    List<AdvertisementServiceModel> advertisements = serviceModel.getAdvertisements();
                    serviceModel.setAdvertisements(advertisements.subList(0, Math.min(advertisements.size(), 10)));
                    return serviceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryServiceModel> getAll() {
        if (repository.count() == 0) {
            seedCategories();
        }
        return repository.findAll().stream()
                .map(x -> mapper.map(x, CategoryServiceModel.class))
                .collect(Collectors.toList());
    }

    private void seedCategories() {
        Photo photo = new Photo();
        photo.setUrl(DEFAULT_CATEGORY_PHOTO_CLOUD_URL);
        photo.setIdInCloud(DEFAULT_CATEGORY_ID_CLOUD);
        photoRepository.save(photo);
        Category category = new Category();
        category.setName(DEFAULT_CATEGORY);
        category.setPhoto(photo);
        repository.save(category);
    }
}
