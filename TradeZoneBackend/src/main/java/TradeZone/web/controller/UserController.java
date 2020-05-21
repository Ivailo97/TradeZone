package TradeZone.web.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import TradeZone.data.model.rest.PasswordUpdate;
import TradeZone.data.model.rest.ProfileUpdate;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.view.AdvertisementListViewModel;
import TradeZone.data.model.view.ProfileViewModel;
import TradeZone.data.model.view.ProfileTableViewModel;
import TradeZone.service.ProfileService;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api/user")
@RestController
@AllArgsConstructor
public class UserController {

    private final ProfileService profileService;

    private final ModelMapper mapper;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProfileViewModel> userProfile(@RequestParam String username) {

        return ResponseEntity.of(profileService.getUserProfileByUsername(username)
                .map(x -> {
                    ProfileViewModel viewModel = mapper.map(x, ProfileViewModel.class);
                    viewModel.setCreatedAdvertisements(mapServiceAdvertisementsToView(x.getCreatedAdvertisements()));
                    viewModel.setFavorites(mapServiceAdvertisementsToView(x.getFavorites()));
                    viewModel.setRoles(x.getUser().getRoles().stream().map(r -> r.getRoleName().name()).collect(Collectors.toList()));
                    return viewModel;
                }));
    }

    @GetMapping("/profile/is-completed")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> profileIsCompleted(@RequestParam String username) {
        return ResponseEntity.ok(profileService.isCompleted(username));
    }

    @PatchMapping("/profile/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HttpStatus> updateProfile(@RequestBody ProfileUpdate update) {
        String responseMessage = profileService.update(update);
        HttpStatus status = responseMessage.contains("FAIL") ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return new ResponseEntity<>(status);
    }

    //good example of rest controller method
    @PatchMapping("/profile/update-password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseMessage> updatePassword(@RequestBody PasswordUpdate update) {

        ResponseMessage responseMessage = profileService.updatePassword(update);
        HttpStatus status = responseMessage.getMessage().contains("FAIL") ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return new ResponseEntity<>(responseMessage, status);
    }

    @PatchMapping(value = "/profile/update-picture/{username}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseMessage> updatePicture(@PathVariable("username") String username,
                                                         @RequestPart("image") MultipartFile file) {

        ResponseMessage responseMessage = profileService.updatePicture(username, file);

        HttpStatus status = responseMessage.getMessage().contains("FAIL") ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return new ResponseEntity<>(responseMessage, status);
    }

    @PostMapping("/profile/add-favorite")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addToFavorite(@RequestBody Map<String, Object> requestBody) {

        String username = String.valueOf(requestBody.get("username"));
        Long addId = Long.parseLong(String.valueOf(requestBody.get("advertisementId")));
        ResponseMessage responseMessage = profileService.addFavorite(username, addId);
        HttpStatus status = responseMessage.getMessage().contains("FAIL") ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return new ResponseEntity<>(status);
    }

    @PostMapping("/profile/remove-favorite")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeFavorite(@RequestBody Map<String, Object> requestBody) {

        String username = String.valueOf(requestBody.get("username"));
        Long addId = Long.parseLong(String.valueOf(requestBody.get("advertisementId")));

        ResponseMessage responseMessage = profileService.removeFavorite(username, addId);
        HttpStatus status = responseMessage.getMessage().contains("FAIL") ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return new ResponseEntity<>(status);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProfileTableViewModel>> adminAccess() {

        return new ResponseEntity<>(this.profileService.getAll().stream()
                .map(x -> {
                    ProfileTableViewModel viewModel = mapper.map(x, ProfileTableViewModel.class);
                    viewModel.setRole(profileService.getTopRole(x.getUser().getRoles()));
                    return viewModel;
                })
                .collect(Collectors.toList()), HttpStatus.ACCEPTED);
    }

    @PostMapping("/profile/disconnect")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody String username) {
        profileService.disconnect(username);
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
