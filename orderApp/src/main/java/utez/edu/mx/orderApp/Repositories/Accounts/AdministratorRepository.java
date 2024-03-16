package utez.edu.mx.orderApp.Repositories.Accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Accounts.Administrator;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Optional<Administrator> findByAdminName(String username);
}
