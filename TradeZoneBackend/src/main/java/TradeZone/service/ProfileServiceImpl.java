package TradeZone.service;

import TradeZone.data.model.entity.*;
import TradeZone.data.model.service.*;
import TradeZone.data.model.view.ProfileConversationViewModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import TradeZone.data.model.rest.PasswordUpdate;
import TradeZone.data.model.rest.ProfileUpdate;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.service.validation.ProfileUpdateValidationService;
import TradeZone.data.repository.AdvertisementRepository;
import TradeZone.data.repository.UserProfileRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private static final String FAIL = "FAIL";
    private static final String WRONG_OLD_PASSWORD = "FAIL -> WRONG OLD PASSWORD";
    private static final String PROFILE_NOT_FOUND = "PROFILE WITH USERNAME %s not found";
    private static final String CONFIRM_PASSWORD_DOESNT_MATCH = "FAIL -> CONFIRM NEW PASSWORD DOES NOT MATCH";

    private static final String SUCCESS = "SUCCESS";

    private final SimpMessagingTemplate template;

    private final ProfileUpdateValidationService profileUpdateValidationService;

    private final PhotoService photoService;

    private final UserProfileRepository userProfileRepository;

    private final AdvertisementRepository advertisementRepository;

    private final BCryptPasswordEncoder encoder;

    private final ModelMapper mapper;

    @Override
    public Optional<ProfileServiceModel> getUserProfileByUsername(String username) {
        return userProfileRepository.findByUserUsername(username)
                .map(x -> mapper.map(x, ProfileServiceModel.class));
    }


    @Override
    public ProfileServiceModel disconnect(String username) {

        UserProfile userProfile = userProfileRepository
                .findByUserUsername(username)
                .orElseThrow();

        if (!userProfile.getConnected()) {
            return null;
        }

        userProfile.setConnected(false);

        userProfile = userProfileRepository.save(userProfile);

        ProfileConversationViewModel viewModel = mapper.map(userProfile, ProfileConversationViewModel.class);

        template.convertAndSend("/channel/logout", viewModel);

        return mapper.map(userProfile, ProfileServiceModel.class);
    }

    @Override
    public Boolean isCompleted(String username) {

        return userProfileRepository.findByUserUsername(username)
                .orElseThrow((EntityNotFoundException::new))
                .getIsCompleted();
    }

    @Override
    public String getTopRole(Set<RoleServiceModel> roles) {
        String role = "User";

        if (roles.stream().anyMatch(x -> x.getRoleName().name().equals("ROLE_ADMIN"))) {
            role = "Admin";
        } else if (roles.stream().anyMatch(x -> x.getRoleName().name().equals("ROLE_MODERATOR"))) {
            role = "Moderator";
        }

        return role;
    }

    @Override
    public List<ProfileServiceModel> getAll() {
        return this.userProfileRepository.findAll().stream()
                .map(x -> mapper.map(x, ProfileServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public String update(ProfileUpdate update) {

        UserProfile profile = userProfileRepository.findById(update.getId()).orElse(null);

        if (profile == null || !profileUpdateValidationService.isValid(update)) {
            return FAIL;
        }

        profile.setFirstName(update.getFirstName());
        profile.setLastName(update.getLastName());
        profile.setCity(update.getCity());
        profile.setCountry(update.getCountry());
        profile.setAboutMe(update.getAboutMe());
        profile.setIsCompleted(true);
        userProfileRepository.save(profile);
        return SUCCESS;
    }

    @Override
    public ResponseMessage updatePicture(String username, MultipartFile file) {

        UserProfile profile = userProfileRepository.findByUserUsername(username).orElse(null);

        if (profile == null) {
            return new ResponseMessage(FAIL);
        }

        Photo photo = profile.getPhoto();

        PhotoServiceModel photoServiceModel = photoService.upload(file);
        Photo newPhoto = mapper.map(photoServiceModel, Photo.class);
        profile.setPhoto(newPhoto);

        userProfileRepository.save(profile);

        if (photo != null) {
            photoService.delete(photo.getId());
        }


        return new ResponseMessage(SUCCESS);
    }

    @Override
    public ResponseMessage addFavorite(String username, Long addId) {
        UserProfile userProfile = userProfileRepository.findByUserUsername(username).orElse(null);
        Advertisement advertisement = advertisementRepository.findById(addId).orElse(null);
        if (userProfile == null || advertisement == null || profileAlreadyLikedTheAdvertisement(userProfile, advertisement)) {
            return new ResponseMessage(FAIL);
        }
        userProfile.getFavorites().add(advertisement);
        userProfileRepository.save(userProfile);
        return new ResponseMessage(SUCCESS);
    }

    @Override
    public ResponseMessage removeFavorite(String username, Long addId) {
        UserProfile userProfile = userProfileRepository.findByUserUsername(username).orElse(null);
        Advertisement advertisement = advertisementRepository.findById(addId).orElse(null);

        if (userProfile == null || advertisement == null || profileFavoritesDoesntContainTheAdvertisement(userProfile, advertisement)) {
            return new ResponseMessage(FAIL);
        }
        userProfile.getFavorites().remove(advertisement);
        userProfileRepository.save(userProfile);
        return new ResponseMessage(SUCCESS);
    }

    @Override
    public ResponseMessage updatePassword(PasswordUpdate update) {

        UserProfile profile = userProfileRepository.findById(update.getId()).orElse(null);

        if (profile == null) {
            return new ResponseMessage(FAIL);
        }

        if (!encoder.matches(update.getOldPassword(), profile.getUser().getPassword())) {
            return new ResponseMessage(WRONG_OLD_PASSWORD);
        }

        if (!update.getNewPassword().equals(update.getConfirmNewPassword())) {
            return new ResponseMessage(CONFIRM_PASSWORD_DOESNT_MATCH);
        }

        profile.getUser().setPassword(encoder.encode(update.getNewPassword()));

        userProfileRepository.save(profile);
        return new ResponseMessage(SUCCESS);
    }

    private boolean profileAlreadyLikedTheAdvertisement(UserProfile profile, Advertisement advertisement) {
        return profile.getFavorites().stream().anyMatch(x -> x.getId().equals(advertisement.getId()));
    }

    private boolean profileFavoritesDoesntContainTheAdvertisement(UserProfile profile, Advertisement advertisement) {
        return profile.getFavorites().stream().noneMatch(x -> x.getId().equals(advertisement.getId()));
    }
}
