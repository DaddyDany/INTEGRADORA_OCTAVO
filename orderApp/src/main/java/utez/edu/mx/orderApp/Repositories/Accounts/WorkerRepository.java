package utez.edu.mx.orderApp.Repositories.Accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Accounts.Worker;

import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByWorkerName(String username);
}
