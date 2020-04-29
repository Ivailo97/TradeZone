package TradeZone.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import TradeZone.data.model.entity.Advertisement;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    List<Advertisement> findAllByPriceBetween(BigDecimal min, BigDecimal max);

    List<Advertisement> findAllByPriceBetweenAndTitleContaining(BigDecimal min, BigDecimal max, String search);

    List<Advertisement> findAllByCategoryNameAndPriceBetween(String categoryName, BigDecimal min, BigDecimal max);

    List<Advertisement> findAllByCategoryNameAndTitleContainingAndPriceBetween(String categoryName, String search, BigDecimal min, BigDecimal max);
}
