package utez.edu.mx.orderApp.Models.Orders;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Packages.Package;

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
}
