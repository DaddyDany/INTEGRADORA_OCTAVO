package utez.edu.mx.orderApp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.CommonUser;

@Repository
public interface CommonUserRepository extends JpaRepository<CommonUser, Long> {
}
