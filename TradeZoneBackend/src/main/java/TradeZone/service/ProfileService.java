package TradeZone.service;

import org.springframework.web.multipart.MultipartFile;
import TradeZone.data.model.rest.PasswordUpdate;
import TradeZone.data.model.rest.ProfileUpdate;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.ProfileServiceModel;
import TradeZone.data.model.service.RoleServiceModel;

import java.util.List;
import java.util.Set;

public interface ProfileService {

    ProfileServiceModel getUserProfileByUsername(String username);

    ProfileServiceModel disconnect(String username);

    Boolean isCompleted(String username);

    String getTopRole(Set<RoleServiceModel> roles);

    List<ProfileServiceModel> getAll();

    ProfileServiceModel update(ProfileUpdate update);

    ProfileServiceModel updatePicture(String username, MultipartFile file);

    ProfileServiceModel addFavorite(String username, Long addId);

    ProfileServiceModel removeFavorite(String username, Long addId);

    ProfileServiceModel updatePassword(PasswordUpdate update);
}
