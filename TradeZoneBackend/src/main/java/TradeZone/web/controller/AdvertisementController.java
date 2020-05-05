package TradeZone.web.controller;

import TradeZone.data.error.exception.AdvertisementNotValidException;
import TradeZone.data.error.exception.EntityNotFoundException;
import TradeZone.data.error.exception.NotAllowedException;
import TradeZone.data.model.rest.*;
import TradeZone.data.model.rest.search.*;
import TradeZone.service.MappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import TradeZone.data.model.enums.Condition;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.view.AdvertisementDetailsViewModel;
import TradeZone.data.model.view.AdvertisementListViewModel;
import TradeZone.data.model.view.AdvertisementToEditViewModel;
import TradeZone.data.model.view.PhotoViewModel;
import TradeZone.service.AdvertisementService;
import TradeZone.service.PhotoService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api")
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final PhotoService photoService;
    private final MappingService mappingService;

    @GetMapping("/all")
    public ResponseEntity<List<AdvertisementListViewModel>> showAds(AlmostFullSearchRequest searchRequest) {
        return ResponseEntity.ok(mappingService.mapServiceAdvertisementsToView(advertisementService.getAllByAlmostFullSearch(searchRequest)));
    }

    @GetMapping("/category-and-text")
    public ResponseEntity<List<AdvertisementListViewModel>> filterByCategoryAndTitle(FullSearchRequest search) {
        return ResponseEntity.ok(mappingService.mapServiceAdvertisementsToView(advertisementService.getAllByFullSearch(search)));
    }

    @GetMapping("/count-price-condition")
    public ResponseEntity<Long> countByPriceInRangeAndCondition(ConditionSearch search) {
        return ResponseEntity.ok(advertisementService.countByPriceBetweenAndCondition(search));
    }

    @GetMapping("/count-full-search")
    public ResponseEntity<Long> countByAll(SearchRequest params) {
        return ResponseEntity.ok(advertisementService.countByCategoryTitleContainingPriceBetweenAndCondition(params));
    }

    @GetMapping("/count-category")
    public ResponseEntity<Long> count(CategorySearchRequest search) {
        return ResponseEntity.ok(advertisementService.countByCategory(search));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AdvertisementCreateModel restModel)
            throws EntityNotFoundException, AdvertisementNotValidException {
        advertisementService.create(restModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<AdvertisementToEditViewModel> edit(@PathVariable Long id) throws EntityNotFoundException {
        AdvertisementServiceModel serviceModel = advertisementService.getById(id);
        AdvertisementToEditViewModel viewModel = mappingService.getMapper().map(serviceModel, AdvertisementToEditViewModel.class);
        viewModel.setCreator(serviceModel.getCreator().getUser().getUsername());
        return ResponseEntity.ok(viewModel);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editConfirm(@RequestBody AdvertisementEditedModel editedModel)
            throws EntityNotFoundException, NotAllowedException {

        advertisementService.edit(editedModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(Principal principal, DeleteAdvRequest deleteRequest)
            throws EntityNotFoundException, NotAllowedException {
        String principalName = principal.getName();
        advertisementService.delete(principalName, deleteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<AdvertisementDetailsViewModel> details(@PathVariable Long id) throws EntityNotFoundException {
        AdvertisementServiceModel serviceModel = advertisementService.getById(id);
        AdvertisementDetailsViewModel viewModel = mappingService.getMapper().map(serviceModel, AdvertisementDetailsViewModel.class);
        viewModel.setCreator(serviceModel.getCreator().getUser().getUsername());
        viewModel.setImages(mappingService.getMapper().map(serviceModel.getPhotos(), PhotoViewModel[].class));
        return ResponseEntity.ok(viewModel);
    }

    @GetMapping("/conditions/all")
    public ResponseEntity<String[]> conditions() {
        return ResponseEntity.ok(Arrays.stream(Condition.values()).map(Enum::name).toArray(String[]::new));
    }

    @PatchMapping("/increase-views/{id}")
    public ResponseEntity<?> updateViews(@PathVariable Long id, @RequestBody ViewsUpdate update) throws EntityNotFoundException {
        advertisementService.updateViews(id, update);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete-image")
    public ResponseEntity<?> deletePhoto(DeleteAdvImageRequest deleteRequest)
            throws EntityNotFoundException {

        advertisementService.detachPhoto(deleteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/upload-images")
    public ResponseEntity<?> uploadPhotos(@RequestBody ImagesToUploadModel images) {

        ResponseMessage responseMessage = photoService.uploadAdvertisementPhotos(images);

        HttpStatus status = responseMessage.getMessage().contains("FAIL") ?
                HttpStatus.BAD_REQUEST : HttpStatus.ACCEPTED;

        return new ResponseEntity<>(status);
    }
}
