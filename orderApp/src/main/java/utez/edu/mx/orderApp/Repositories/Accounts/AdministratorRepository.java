package utez.edu.mx.orderApp.Repositories.Accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Accounts.Administrator;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Optional<Administrator> findByAdminName(String username);

    @Query("SELECT a FROM Administrator a WHERE a.role.roleName = :roleName")
    List<Administrator> findAllByRoleName(@Param("roleName") String roleName);

}
