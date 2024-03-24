package utez.edu.mx.orderapp.repositories.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.orders.Order;

import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderDate(Date orderDate);
}
