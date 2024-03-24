package utez.edu.mx.orderapp.repositories.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.accounts.CommonUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommonUserRepository extends JpaRepository<CommonUser, Long> {
    Optional<CommonUser> findByUserName(String username);
    @Query("SELECT c FROM CommonUser c WHERE c.role.roleName = :roleName")
    List<CommonUser> findAllByRoleName(@Param("roleName") String roleName);
}
