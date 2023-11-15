package xcode.marsiajar.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.marsiajar.domain.model.CategoryModel;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, String> {

   CategoryModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<CategoryModel> findByDeletedAtIsNullOrderByCreatedAtDesc();

   @Query(value = "SELECT * FROM t_cateogry " +
           " WHERE secure_id = :category AND deleted_at IS NULL", nativeQuery = true)
   List<CategoryModel> getCategories(String category);
}
