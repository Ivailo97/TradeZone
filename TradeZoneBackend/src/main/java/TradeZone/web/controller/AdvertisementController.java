package TradeZone.web.controller;

import TradeZone.data.model.enums.DeliveryType;
import TradeZone.data.model.rest.*;
import TradeZone.data.model.rest.search.*;
import TradeZone.service.MappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import TradeZone.data.model.enums.Condition;
import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.view.AdvertisementDetailsViewModel;
import TradeZone.data.model.view.AdvertisementListViewModel;
import TradeZone.data.model.view.AdvertisementToEditViewModel;
import TradeZone.data.model.view.PhotoViewModel;
import TradeZone.service.AdvertisementService;
import TradeZone.service.PhotoService;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;
//import org.springframework.web.reactive.function.client.WebClient;

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
    //private final WebClient.Builder builder;

    @GetMapping("/search")
    public ResponseEntity<List<AdvertisementListViewModel>> filterByCategoryAndTitle(FullSearchRequest search) {
        // micro service async call done sync;
        // like async and await in javascript;
//        Object obj = builder.build()
//                .get()
//                .uri("https://{registeredMicroserviceName}/{servicePath}")
//                .retrieve()
//                .bodyToMono(Object.class)
//                .block();
        //block = await;
        List<AdvertisementServiceModel> advertisements = advertisementService.getAllByFullSearch(search).getContent();
        return ok(mappingService.mapServiceAdvertisementsToView(advertisements));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countByAll(SearchRequest params) {
        return ok(advertisementService.getCountBySearch(params));
    }

    @GetMapping("/created-by/{username}")
    public ResponseEntity<List<AdvertisementListViewModel>> getByCreator(SpecificSearch specificSearch, @PathVariable String username) {
        List<AdvertisementServiceModel> serviceModels = advertisementService.findByCreatorUsernameExcept(specificSearch, username);
        List<AdvertisementListViewModel> viewModels = mappingService.mapServiceAdvertisementsToView(serviceModels);
        return ok(viewModels);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@RequestBody AdvertisementCreateModel restModel) {
        advertisementService.create(restModel);
        return ok(CREATED);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<AdvertisementToEditViewModel> edit(@PathVariable Long id) {
        AdvertisementServiceModel serviceModel = advertisementService.getById(id);
        AdvertisementToEditViewModel viewModel = mappingService.getMapper().map(serviceModel, AdvertisementToEditViewModel.class);
        viewModel.setCreator(serviceModel.getCreator().getUser().getUsername());
        return ok(viewModel);
    }

    @PutMapping("/edit")
    public ResponseEntity<HttpStatus> editConfirm(@RequestBody AdvertisementEditedModel editedModel) {
        advertisementService.edit(editedModel);
        return ok(OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> delete(Principal principal, DeleteAdvRequest deleteRequest) {
        String principalName = principal.getName();
        advertisementService.delete(principalName, deleteRequest);
        return ok(OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<AdvertisementDetailsViewModel> details(@PathVariable Long id) {
        AdvertisementServiceModel serviceModel = advertisementService.getById(id);
        AdvertisementDetailsViewModel viewModel = mappingService.getMapper().map(serviceModel, AdvertisementDetailsViewModel.class);
        viewModel.setImages(mappingService.getMapper().map(serviceModel.getPhotos(), PhotoViewModel[].class));
        return ok(viewModel);
    }

    @GetMapping("/conditions/all")
    public ResponseEntity<String[]> conditions() {
        return ok(Arrays.stream(Condition.values()).map(Enum::name).toArray(String[]::new));
    }

    @GetMapping("/deliveries/all")
    public ResponseEntity<String[]> deliveries() {
        return ok(Arrays.stream(DeliveryType.values()).map(Enum::name).toArray(String[]::new));
    }

    @PatchMapping("/increase-views/{id}")
    public ResponseEntity<HttpStatus> updateViews(@PathVariable Long id, @RequestBody ViewsUpdate update) {
        advertisementService.updateViews(id, update);
        return ok(OK);
    }

    @DeleteMapping("/delete-image")
    public ResponseEntity<HttpStatus> deletePhoto(DeleteAdvImageRequest deleteRequest) {
        advertisementService.deletePhoto(deleteRequest);
        return ok(OK);
    }

    @PatchMapping("/upload-images")
    public ResponseEntity<HttpStatus> uploadPhotos(@RequestBody ImagesToUploadModel images) {
        photoService.uploadAdvertisementPhotos(images);
        return ok(OK);
    }
}
