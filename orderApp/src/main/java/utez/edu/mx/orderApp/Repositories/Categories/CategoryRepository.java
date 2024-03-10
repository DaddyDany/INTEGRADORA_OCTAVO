package utez.edu.mx.orderApp.Repositories.Categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Categories.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByServiceName(String serviceName);
}
