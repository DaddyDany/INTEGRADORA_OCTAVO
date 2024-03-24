package utez.edu.mx.orderapp.repositories.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.orders.OrderPackage;

@Repository
public interface OrderPackageRepository extends JpaRepository<OrderPackage, Long> {
}
