package utez.edu.mx.orderapp.models.orders;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderapp.models.packages.Package;

@Getter
@Setter
@Entity
@Table(name = "order_packages")
public class OrderPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_package_id")
    private Long orderPackageId;
    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package aPackage;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderPackage(Order order, Package pkg) {
        this.order = order;
        this.aPackage = pkg;
    }

    public OrderPackage() {

    }
}
