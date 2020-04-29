package TradeZone.service;

import TradeZone.data.model.service.AdvertisementServiceModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import TradeZone.data.model.entity.Category;
import TradeZone.data.model.entity.UserProfile;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.CategoryServiceModel;
import TradeZone.data.model.service.validation.CategoryValidationService;
import TradeZone.data.repository.CategoryRepository;
import TradeZone.data.repository.PhotoRepository;
import TradeZone.data.repository.UserProfileRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final String INVALID_MODEL = "FAIL -> INVALID DATA";
    private static final String SUCCESS = "SUCCESS -> CREATED NEW DATA";

    private final CategoryRepository repository;

    private final UserProfileRepository profileRepository;

    private final PhotoRepository photoRepository;

    private final CategoryValidationService validationService;

    private final ModelMapper mapper;

    @Override
    public CategoryServiceModel getById(long id) {

        return this.repository.findById(id)
                .map(x -> this.mapper.map(x, CategoryServiceModel.class))
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    @Transactional
    public ResponseMessage create(CategoryServiceModel category, String creatorUsername) {

        if (!validationService.isValid(category)) {
            return new ResponseMessage(INVALID_MODEL);
        }

        UserProfile userProfile = profileRepository.findByUserUsername(creatorUsername).orElse(null);

        if (userProfile == null) {
            return new ResponseMessage(INVALID_MODEL);
        }

        Category entity = mapper.map(category, Category.class);
        entity.setCreator(userProfile);
        photoRepository.save(entity.getPhoto());
        repository.save(entity);
        return new ResponseMessage(SUCCESS);
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
        return repository.findAll().stream().map(x -> this.mapper.map(x, CategoryServiceModel.class))
                .collect(Collectors.toList());
    }
}
