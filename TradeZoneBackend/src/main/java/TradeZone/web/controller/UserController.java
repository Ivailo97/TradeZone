package TradeZone.web.controller;

import TradeZone.service.MappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import TradeZone.data.model.rest.PasswordUpdate;
import TradeZone.data.model.rest.ProfileUpdate;
import TradeZone.data.model.view.ProfileViewModel;
import TradeZone.data.model.view.ProfileTableViewModel;
import TradeZone.service.ProfileService;

import static org.springframework.http.ResponseEntity.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api/user")
@RestController
@AllArgsConstructor
public class UserController {

    private final ProfileService service;

    private final MappingService mappingService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProfileViewModel> userProfile(@RequestParam String username) {
        return ok(mappingService.mapServiceProfileToView(service.getUserProfileByUsername(username)));
    }

    @GetMapping("/profile/is-completed")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> profileIsCompleted(@RequestParam String username) {
        return ok(service.isCompleted(username));
    }

    @PatchMapping("/profile/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdate update) {
        service.update(update);
        return ok().build();
    }

    @PatchMapping("/profile/update-password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordUpdate update) {
        service.updatePassword(update);
        return ok().build();
    }

    @PatchMapping(value = "/profile/update-picture/{username}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updatePicture(@PathVariable("username") String username,
                                              @RequestPart("image") MultipartFile file) {
        service.updatePicture(username, file);
        return ok().build();
    }

    @PostMapping("/profile/add-favorite")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addToFavorite(@RequestBody Map<String, Object> requestBody) {
        String username = String.valueOf(requestBody.get("username"));
        Long addId = Long.parseLong(String.valueOf(requestBody.get("advertisementId")));
        service.addFavorite(username, addId);
        return ok().build();
    }

    @PostMapping("/profile/remove-favorite")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeFavorite(@RequestBody Map<String, Object> requestBody) {
        String username = String.valueOf(requestBody.get("username"));
        Long addId = Long.parseLong(String.valueOf(requestBody.get("advertisementId")));
        service.removeFavorite(username, addId);
        return ok().build();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProfileTableViewModel>> adminAccess() {
        List<ProfileTableViewModel> viewModels = mappingService.mapServiceToTableViewModel(service.getAll(), service);
        return ok(viewModels);
    }

    @PostMapping("/profile/disconnect")
    public ResponseEntity<Void> logout(@RequestBody String username) {
        service.disconnect(username);
        return ok().build();
    }
}
