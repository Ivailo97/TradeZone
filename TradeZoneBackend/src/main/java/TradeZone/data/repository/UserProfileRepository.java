package TradeZone.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import TradeZone.data.model.entity.UserProfile;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserUsername(String username);

    List<UserProfile> findAllByConnectedTrue();
}
