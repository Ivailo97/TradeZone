package TradeZone.data.model.service;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserServiceModel {

    private Long id;

    private String username;

    private String email;

    private String password;

    private ProfileServiceModel profile;

    private Set<RoleServiceModel> roles;
}
