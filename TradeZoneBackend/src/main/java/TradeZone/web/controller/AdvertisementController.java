package TradeZone.web.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import TradeZone.data.model.enums.Condition;
import TradeZone.data.model.rest.AdvertisementCreateModel;
import TradeZone.data.model.rest.AdvertisementEditedModel;
import TradeZone.data.model.rest.ImagesToUploadModel;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.view.AdvertisementDetailsViewModel;
import TradeZone.data.model.view.AdvertisementListViewModel;
import TradeZone.data.model.view.AdvertisementToEditViewModel;
import TradeZone.data.model.view.PhotoViewModel;
import TradeZone.service.AdvertisementService;
import TradeZone.service.PhotoService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api")
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final PhotoService photoService;
    private final ModelMapper mapper;

    @GetMapping("/all/{category}")
    public List<AdvertisementListViewModel> showAds(@RequestParam BigDecimal min,
                                                    @RequestParam BigDecimal max,
                                                    @RequestParam Integer page,
                                                    @RequestParam String sortBy,
                                                    @RequestParam String order,
                                                    @RequestParam String condition,
                                                    @PathVariable String category) {

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

        List<AdvertisementServiceModel> advertisementServiceModels;

        if (!condition.equals("All")) {
            advertisementServiceModels = advertisementService.getAllByCategoryPriceBetweenAndCondition(min, max, condition, category, pageRequest);
        } else {
            advertisementServiceModels = advertisementService.getAllByCategoryAndPriceBetween(category, min, max, pageRequest);
        }

        return mapServiceAdvertisementsToView(advertisementServiceModels);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        Long count = advertisementService.countOfAll();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count-price-between")
    public ResponseEntity<Long> count(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        Long count = advertisementService.countOfPriceBetween(min, max);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count-price-between-condition")
    public ResponseEntity<Long> count(@RequestParam BigDecimal min,
                                      @RequestParam BigDecimal max,
                                      @RequestParam String condition) {

        Long count;
        if (!condition.equals("All")) {
            count = advertisementService.countOfPriceBetweenAndCondition(min, max, condition);
        } else {
            count = advertisementService.countOfAll();
        }
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count-price-between-condition-search-category")
    public ResponseEntity<Long> count(@RequestParam BigDecimal min,
                                      @RequestParam BigDecimal max,
                                      @RequestParam String condition,
                                      @RequestParam String search,
                                      @RequestParam String category) {

        return ResponseEntity.ok(advertisementService.countByCategoryTitleContainingPriceBetweenAndCondition(category, search, min, max, condition));
    }

    @GetMapping("/count-category")
    public ResponseEntity<Long> count(@RequestParam String category,
                                      @RequestParam BigDecimal min,
                                      @RequestParam BigDecimal max,
                                      @RequestParam String condition) {
        Long count;

        if (condition.equals("All")) {
            count = advertisementService.countByCategoryAndPriceBetween(category, min, max);
        } else {
            count = advertisementService.countByCategoryConditionAndPriceBetween(category, condition, min, max);
        }

        return ResponseEntity.ok(count);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AdvertisementCreateModel restModel) {

        ResponseMessage responseMessage = advertisementService.create(restModel);

        HttpStatus status = responseMessage.getMessage().contains("FAIL") ?
                HttpStatus.BAD_REQUEST : HttpStatus.CREATED;

        return new ResponseEntity<>(status);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<AdvertisementToEditViewModel> edit(@PathVariable Long id) {

        AdvertisementServiceModel serviceModel = advertisementService.getById(id);
        AdvertisementToEditViewModel viewModel = mapper.map(serviceModel, AdvertisementToEditViewModel.class);

        viewModel.setCreator(serviceModel.getCreator().getUser().getUsername());

        return ResponseEntity.ok(viewModel);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editConfirm(@RequestBody AdvertisementEditedModel editedModel) {

        ResponseMessage responseMessage = advertisementService.edit(editedModel);

        HttpStatus status = responseMessage.getMessage().contains("FAIL") ?
                HttpStatus.BAD_REQUEST : HttpStatus.ACCEPTED;

        return new ResponseEntity<>(status);
    }

    @DeleteMapping("/delete/{username}/{id}")
    public ResponseEntity<?> delete(Principal principal, @PathVariable String username, @PathVariable Long id) {

        String principalName = principal.getName();

        ResponseMessage responseMessage = advertisementService.delete(principalName, username, id);

        HttpStatus status = responseMessage.getMessage().contains("FAIL") ?
                HttpStatus.BAD_REQUEST : HttpStatus.ACCEPTED;

        return new ResponseEntity<>(status);
    }

    @GetMapping("/category-and-text")
    public List<AdvertisementListViewModel> filterByCategoryAndTitle(@RequestParam String search,
                                                                     @RequestParam String category,
                                                                     @RequestParam BigDecimal min,
                                                                     @RequestParam BigDecimal max,
                                                                     @RequestParam Integer page,
                                                                     @RequestParam String sortBy,
                                                                     @RequestParam String order,
                                                                     @RequestParam String condition) {

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

        List<AdvertisementServiceModel> advertisementServiceModels;

        if (!condition.equals("All")) {
            advertisementServiceModels = advertisementService.getAllByCategoryTitleContainingPriceBetweenAndCondition(category, search, min, max, condition, pageRequest);
        } else {
            advertisementServiceModels = advertisementService.getAllByCategoryTitleContainingAndPriceBetween(category, search, min, max, pageRequest);
        }

        return mapServiceAdvertisementsToView(advertisementServiceModels);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<AdvertisementDetailsViewModel> details(@PathVariable(name = "id") long id) {

        AdvertisementServiceModel serviceModel = advertisementService.getById(id);
        AdvertisementDetailsViewModel viewModel = mapper.map(serviceModel, AdvertisementDetailsViewModel.class);
        viewModel.setCreator(serviceModel.getCreator().getUser().getUsername());
        viewModel.setImages(mapper.map(serviceModel.getPhotos(), PhotoViewModel[].class));
        return ResponseEntity.ok(viewModel);
    }

    @GetMapping("/conditions/all")
    public String[] conditions() {
        return Arrays.stream(Condition.values()).map(Enum::name).toArray(String[]::new);
    }

    @PatchMapping("/increase-views/{id}")
    public ResponseEntity<?> updateViews(@PathVariable Long id, @RequestBody Map<String, Object> updatedProperties) {
        ResponseMessage responseMessage = advertisementService.increaseViews(id, Long.parseLong(String.valueOf(updatedProperties.get("views"))));
        return ResponseEntity.ok(responseMessage.getMessage());
    }

    @DeleteMapping("/delete-image/{advertisementId}/{username}/{photoId}")
    public ResponseEntity<?> deletePhoto(@PathVariable Long advertisementId, @PathVariable String username, @PathVariable Long photoId) {

        ResponseMessage responseMessage = advertisementService.detachPhoto(username, advertisementId, photoId);

        if (responseMessage.getMessage().contains("SUCCESS")) {
            responseMessage = photoService.delete(photoId);
        }

        HttpStatus status = responseMessage.getMessage().contains("FAIL") ?
                HttpStatus.BAD_REQUEST : HttpStatus.ACCEPTED;

        return new ResponseEntity<>(status);
    }

    @PatchMapping("/upload-images/{advertisementId}/{username}")
    public ResponseEntity<?> uploadPhotos(@PathVariable Long advertisementId, @PathVariable String username, @RequestBody ImagesToUploadModel images) {

        ResponseMessage responseMessage = photoService.uploadAdvertisementPhotos(advertisementId, username, images);

        HttpStatus status = responseMessage.getMessage().contains("FAIL") ?
                HttpStatus.BAD_REQUEST : HttpStatus.ACCEPTED;

        return new ResponseEntity<>(status);
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
