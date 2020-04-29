package TradeZone.web.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import TradeZone.data.model.rest.CategoryCreateModel;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.service.CategoryServiceModel;
import TradeZone.data.model.service.PhotoServiceModel;
import TradeZone.data.model.view.AdvertisementListViewModel;
import TradeZone.data.model.view.CategoryListViewModel;
import TradeZone.data.model.view.CategorySelectViewModel;
import TradeZone.data.model.view.TopCategoryViewModel;
import TradeZone.service.CategoryService;
import TradeZone.service.PhotoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    private final PhotoService photoService;

    private final ModelMapper mapper;

    @GetMapping("/all")
    public List<CategoryListViewModel> categories() {

        return categoryService.getAll().stream()
                .map(x -> mapper.map(x, CategoryListViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/top")
    public ResponseEntity<List<TopCategoryViewModel>> topCategories(@RequestParam(name = "count") Integer count) {

        return new ResponseEntity<>(this.categoryService.getTop(count)
                .stream()
                .map(x -> {
                    TopCategoryViewModel viewModel = mapper.map(x, TopCategoryViewModel.class);
                    viewModel.setAdvertisements(mapServiceAdvertisementsToView(x.getAdvertisements()));
                    return viewModel;
                })
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> create(@RequestBody CategoryCreateModel restModel) {

        CategoryServiceModel categoryServiceModel = mapper.map(restModel, CategoryServiceModel.class);

        PhotoServiceModel photoServiceModel = photoService.create(restModel.getImage());

        categoryServiceModel.setPhoto(photoServiceModel);

        ResponseMessage responseMessage = categoryService.create(categoryServiceModel, restModel.getCreator());

        HttpStatus status = responseMessage.getMessage().contains("FAIL") ?
                HttpStatus.BAD_REQUEST : HttpStatus.CREATED;

        return new ResponseEntity<>(status);
    }

    @GetMapping("/select")
    public List<CategorySelectViewModel> categorySelect() {

        return categoryService.getAll().stream()
                .map(x -> mapper.map(x, CategorySelectViewModel.class))
                .collect(Collectors.toList());
    }

    private List<AdvertisementListViewModel> mapServiceAdvertisementsToView(List<AdvertisementServiceModel> models) {
        return models.stream().map(a -> {
            AdvertisementListViewModel model = mapper.map(a, AdvertisementListViewModel.class);

            if (a.getPhotos().size() != 0) {
                model.setImageUrl(a.getPhotos().get(0).getUrl());
            }

            model.setCreator(a.getCreator().getUser().getUsername());
            model.setProfilesWhichLikedIt(a.getProfilesWhichLikedIt().stream().map(x -> x.getUser().getUsername()).collect(Collectors.toList()));
            return model;
        }).collect(Collectors.toList());
    }
}
