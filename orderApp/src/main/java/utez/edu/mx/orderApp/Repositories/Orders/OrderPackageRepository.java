package utez.edu.mx.orderApp.Repositories.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Orders.OrderPackage;

@Repository
public interface OrderPackageRepository extends JpaRepository<OrderPackage, Long> {
}
