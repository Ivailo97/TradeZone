package TradeZone.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import TradeZone.data.model.entity.Role;
import TradeZone.data.model.enums.RoleName;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);

    List<Role> findAllByNameNotLike(RoleName name);
}

