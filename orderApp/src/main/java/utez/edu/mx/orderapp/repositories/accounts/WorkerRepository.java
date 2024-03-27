package utez.edu.mx.orderapp.repositories.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.accounts.Worker;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByWorkerName(String username);
    Optional<Worker> findByWorkerEmail(String email);
    @Query("SELECT w FROM Worker w WHERE w.role.roleName = :roleName")
    List<Worker> findAllByRoleName(@Param("roleName") String roleName);
}
