package TradeZone.data.repository;

import TradeZone.data.model.enums.Condition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import TradeZone.data.model.entity.Advertisement;

import java.math.BigDecimal;

@Repository
public interface AdvertisementRepository extends PagingAndSortingRepository<Advertisement, Long> {

    Page<Advertisement> findAllByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageRequest);

    Page<Advertisement> findAllByPriceBetweenAndCondition(BigDecimal min, BigDecimal max, Condition condition, Pageable pageRequest);

    Page<Advertisement> findAllByCategoryNameAndPriceBetweenAndCondition(String category, BigDecimal min, BigDecimal max, Condition condition, Pageable pageable);

    Page<Advertisement> findAllByCategoryNameAndPriceBetween(String category, BigDecimal min, BigDecimal max, Pageable pageRequest);

    Page<Advertisement> findAllByCategoryNameAndTitleContainingAndPriceBetween(String categoryName, String search, BigDecimal min, BigDecimal max, Pageable pageable);

    Page<Advertisement> findAllByTitleContainingAndPriceBetweenAndCondition(String search, BigDecimal min, BigDecimal max, Condition condition, Pageable page);

    Long countAdvertisementByPriceBetweenAndCondition(BigDecimal min, BigDecimal max, Condition condition);

    Long countByPriceBetweenAndTitleContaining(BigDecimal min, BigDecimal max, String search);

    Long countAdvertisementByPriceBetween(BigDecimal min, BigDecimal max);

    Long countAdvertisementByCategoryNameAndPriceBetween(String categoryName, BigDecimal min, BigDecimal max);

    Long countAdvertisementByCategoryNameAndConditionAndPriceBetween(String categoryName, Condition condition, BigDecimal min, BigDecimal max);

    Page<Advertisement> findAllByPriceBetweenAndTitleContaining(BigDecimal min, BigDecimal max, String searchText, Pageable page);

    Page<Advertisement> findAllByTitleContainingAndCategoryNameAndPriceBetweenAndCondition(String search, String category, BigDecimal min, BigDecimal max, Condition condition, Pageable page);

    Long countByPriceBetweenAndTitleContainingAndCondition(BigDecimal min, BigDecimal max, String search, Condition condition);

    Long countByPriceBetweenAndTitleContainingAndCategoryName(BigDecimal min, BigDecimal max, String search, String category);

    Long countByPriceBetweenAndTitleContainingAndCategoryNameAndCondition(BigDecimal min, BigDecimal max, String search, String category, Condition condition);
}
