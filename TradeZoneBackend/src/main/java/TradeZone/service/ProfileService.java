package TradeZone.service;

import org.springframework.web.multipart.MultipartFile;
import TradeZone.data.model.rest.PasswordUpdate;
import TradeZone.data.model.rest.ProfileUpdate;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.ProfileServiceModel;
import TradeZone.data.model.service.RoleServiceModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProfileService {

    Optional<ProfileServiceModel> getUserProfileByUsername(String username);

    String getTopRole(Set<RoleServiceModel> roles);

    List<ProfileServiceModel> getAll();

    String update(ProfileUpdate update);

    ResponseMessage updatePicture(String username, MultipartFile file);

    ResponseMessage addFavorite(String username, Long addId);

    ResponseMessage removeFavorite(String username, Long addId);

    ResponseMessage updatePassword(PasswordUpdate update);
}
