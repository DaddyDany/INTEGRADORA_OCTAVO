package utez.edu.mx.orderApp.Repositories.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Orders.Order;

import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderDate(Date orderDate);
}
