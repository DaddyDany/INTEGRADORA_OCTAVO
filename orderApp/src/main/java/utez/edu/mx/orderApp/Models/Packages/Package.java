package utez.edu.mx.orderApp.Models.Packages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Categories.Category;
import utez.edu.mx.orderApp.Models.Orders.OrderPackage;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "packages")
@NoArgsConstructor
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Long packageId;
    @Column(name = "package_name")
    private String packageName;
    @Column(name = "package_description")
    private String packageDescription;
    @Column(name = "package_price")
    private Float packagePrice;
    @Column(name = "package_state")
    private Boolean packageState;
    @Column(name = "designated_hours")
    private Integer designatedHours;
    @Column(name = "workers_number")
    private Integer workersNumber;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Category category;
    @JsonIgnore
    @OneToMany(mappedBy = "aPackage")
    private List<OrderPackage> orderPackages;

    @JsonIgnore
    @OneToMany(mappedBy = "aPackage")
    private List<PackageCombo> packageCombos;

    public Package(Long packageId, String packageName, String packageDescription, Float packagePrice, Boolean packageState, Integer designatedHours, Integer workersNumber, Category category, List<OrderPackage> orderPackages, List<PackageCombo> packageCombos) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.packageDescription = packageDescription;
        this.packagePrice = packagePrice;
        this.packageState = packageState;
        this.designatedHours = designatedHours;
        this.workersNumber = workersNumber;
        this.category = category;
        this.orderPackages = orderPackages;
        this.packageCombos = packageCombos;
    }
}
