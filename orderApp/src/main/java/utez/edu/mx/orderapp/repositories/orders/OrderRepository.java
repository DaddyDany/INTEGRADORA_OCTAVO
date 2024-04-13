package utez.edu.mx.orderapp.repositories.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.orders.Order;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCommonUserCommonUserId(Long commonUserId);
    @Query("select wo.order from WorkerOrder wo where wo.worker.id = :workerId")
    List<Order> findByWorkerId(@Param("workerId") Long workerId);
    Optional<Order> findBySessionId(String sessionId);
}
