package utez.edu.mx.orderapp.repositories.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.orders.OrderPackage;

import java.util.List;

@Repository
public interface OrderPackageRepository extends JpaRepository<OrderPackage, Long> {
    List<OrderPackage> findByOrderOrderId(Long orderId);
}
