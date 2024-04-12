package utez.edu.mx.orderapp.repositories.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.orders.OrderCombo;
import utez.edu.mx.orderapp.models.orders.OrderPackage;

import java.util.List;

@Repository
public interface OrderComboRepository extends JpaRepository<OrderCombo, Long> {
    List<OrderCombo> findByOrderOrderId(Long orderId);

}
