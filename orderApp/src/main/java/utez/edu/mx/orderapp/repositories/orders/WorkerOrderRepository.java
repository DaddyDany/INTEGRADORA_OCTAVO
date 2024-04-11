package utez.edu.mx.orderapp.repositories.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.orders.WorkerOrder;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkerOrderRepository extends JpaRepository<WorkerOrder, Long> {
    List<WorkerOrder> findByWorkerOrderId(Long orderId);
    @Query("SELECT wo FROM WorkerOrder wo WHERE wo.worker.workerId = :workerId AND FUNCTION('DATE', wo.order.orderDate) = :date")
    List<WorkerOrder> findByWorkerIdAndOrderDate(Long workerId, LocalDate date);
    List<WorkerOrder> findByOrderOrderId(Long orderId);
}
