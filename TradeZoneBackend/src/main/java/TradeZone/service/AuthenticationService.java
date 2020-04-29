package TradeZone.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import TradeZone.data.model.rest.message.response.JwtResponse;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.UserServiceModel;


public interface AuthenticationService extends UserDetailsService {

    ResponseMessage register(UserServiceModel user);

    JwtResponse login(UserServiceModel user);
}
