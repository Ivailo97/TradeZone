package TradeZone.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import TradeZone.data.model.entity.Role;
import TradeZone.data.model.enums.RoleName;
import TradeZone.data.repository.RoleRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final List<Role> roles = List.of(new Role(RoleName.ROLE_USER),
            new Role(RoleName.ROLE_MODERATOR), new Role(RoleName.ROLE_ADMIN));

    private final RoleRepository roleRepository;

    @Override
    public void seed() {
        roleRepository.saveAll(roles);
    }

    @Override
    public boolean rolesAreSeeded() {
        return roleRepository.count() != 0;
    }
}
