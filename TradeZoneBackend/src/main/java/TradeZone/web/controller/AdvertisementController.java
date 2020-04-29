package TradeZone.web.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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

    @GetMapping("/all")
    public List<AdvertisementListViewModel> showAds(@RequestParam BigDecimal min,
                                                    @RequestParam BigDecimal max) {

        return mapServiceAdvertisementsToView(advertisementService.getAllWithPriceBetween(min, max));
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

    @GetMapping("/filter")
    public List<AdvertisementListViewModel> filterAds(@RequestParam String title,
                                                      @RequestParam BigDecimal min,
                                                      @RequestParam BigDecimal max) {

        return mapServiceAdvertisementsToView(advertisementService.getByTitleContainingAndPriceBetween(title, min, max));
    }

    @GetMapping("/{cat}")
    public List<AdvertisementListViewModel> filterByCategory(@PathVariable(name = "cat") String cat,
                                                             @RequestParam BigDecimal min,
                                                             @RequestParam BigDecimal max) {

        if (cat.equals("All")) {
            return mapServiceAdvertisementsToView(advertisementService.getAllWithPriceBetween(min, max));
        }

        return mapServiceAdvertisementsToView(advertisementService.getAllByCategoryAndPriceBetween(cat, min, max));
    }

    @GetMapping("/category-and-text")
    public List<AdvertisementListViewModel> filterByCategoryAndTitle(@RequestParam String search,
                                                                     @RequestParam String category,
                                                                     @RequestParam BigDecimal min,
                                                                     @RequestParam BigDecimal max) {

        return mapServiceAdvertisementsToView(advertisementService.getAllByCategoryTitleContainingAndPriceBetween(category, search, min, max));
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
