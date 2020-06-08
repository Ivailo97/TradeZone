package TradeZone.service;

import TradeZone.data.model.rest.RoleChange;
import org.springframework.security.core.userdetails.UserDetailsService;
import TradeZone.data.model.rest.message.response.JwtResponse;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.UserServiceModel;

import java.security.Principal;

public interface AuthenticationService extends UserDetailsService {

    ResponseMessage register(UserServiceModel user);

    JwtResponse login(UserServiceModel user);

    UserServiceModel changeRole(RoleChange change, Principal principal);
}
