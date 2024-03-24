package utez.edu.mx.orderapp.repositories.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.categories.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByServiceName(String serviceName);
}
