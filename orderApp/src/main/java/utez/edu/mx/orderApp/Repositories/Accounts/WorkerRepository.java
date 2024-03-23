package utez.edu.mx.orderApp.Repositories.Accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Accounts.Administrator;
import utez.edu.mx.orderApp.Models.Accounts.Worker;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByWorkerName(String username);

    @Query("SELECT w FROM Worker w WHERE w.role.roleName = :roleName")
    List<Worker> findAllByRoleName(@Param("roleName") String roleName);
}
