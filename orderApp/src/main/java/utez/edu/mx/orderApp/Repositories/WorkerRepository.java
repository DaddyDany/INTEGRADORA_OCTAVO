package utez.edu.mx.orderApp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
}
