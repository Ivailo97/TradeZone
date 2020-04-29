package TradeZone.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import TradeZone.data.model.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo,Long> {
}
