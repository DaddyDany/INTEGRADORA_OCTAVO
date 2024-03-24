package utez.edu.mx.orderapp.repositories.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.orders.OrderCombo;

@Repository
public interface OrderComboRepository extends JpaRepository<OrderCombo, Long> {
}
