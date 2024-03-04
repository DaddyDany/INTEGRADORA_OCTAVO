package utez.edu.mx.orderApp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.UserAttribute;

@Repository
public interface UserAttributeRepository extends JpaRepository<UserAttribute, Long> {
    UserAttribute findByUserName(String username);
    boolean existsByUserName(String username);
}
