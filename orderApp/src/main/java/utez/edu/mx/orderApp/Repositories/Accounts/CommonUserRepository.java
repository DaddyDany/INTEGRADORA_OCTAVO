package utez.edu.mx.orderApp.Repositories.Accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Accounts.CommonUser;

import java.util.Optional;

@Repository
public interface CommonUserRepository extends JpaRepository<CommonUser, Long> {
    Optional<CommonUser> findByUserName(String username);
}
