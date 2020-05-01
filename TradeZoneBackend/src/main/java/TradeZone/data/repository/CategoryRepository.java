package TradeZone.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import TradeZone.data.model.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long id);

    @Query(value = "SELECT * ,COUNT(*) cnt \n" +
            "FROM categories c \n" +
            "JOIN advertisements a on c.id = a.category_id \n" +
            "GROUP BY c.name \n" +
            "ORDER BY cnt DESC \n" +
            "LIMIT :count", nativeQuery = true)
    List<Category> findTopOrderedByAdvertisementsCountDesc(@Param("count") Integer count);

    Boolean existsByName(String categoryName);
}
