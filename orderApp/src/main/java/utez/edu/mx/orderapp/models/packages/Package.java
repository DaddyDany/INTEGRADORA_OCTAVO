package utez.edu.mx.orderapp.models.packages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderapp.models.categories.Category;
import utez.edu.mx.orderapp.models.orders.OrderPackage;

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

    @NotNull(message = "El nombre no debe ser nulo")
    @NotBlank(message = "El nombre no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ.,\\s]*$", message = "El campo solo puede contener letras, puntos, comas, y caracteres acentuados")
    @Column(name = "package_name")
    private String packageName;

    @NotNull(message = "La descripción no debe ser nula")
    @NotBlank(message = "La descripción no debe ir vacía")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ.,\\s]*$", message = "El campo solo puede contener letras, puntos, comas, y caracteres acentuados")
    @Size(min = 50, max = 3000, message = "La descripción del servicio debe tener como mínimo 50 y como máximo 3000 caracteres")
    @Column(name = "package_description")
    private String packageDescription;

    @NotNull(message = "El precio no debe ser nulo")
    @Max(value = 15000, message = "El precio no debe ser superior a 15000, dudo que alguien pague eso")
    @Min(value = 0, message = "El precio no debe ser negativo")
    @Column(name = "package_price")
    private Long packagePrice;
    @Column(name = "package_state")
    private Boolean packageState;

    @NotNull(message = "Las horas no deben ser nulas")
    @Min(value = 0, message = "Las horas no deben ser negativas")
    @Column(name = "designated_hours")
    private Integer designatedHours;

    @NotNull(message = "El número de trabajadores no debe ser nulo")
    @Min(value = 0, message = "El número de trabajadores no debe ser negativo")
    @Column(name = "workers_number")
    private Integer workersNumber;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Category category;
    @JsonIgnore
    @OneToMany(mappedBy = "aPackage")
    private List<OrderPackage> orderPackages;
    @JsonIgnore
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL)
    private List<PackageCombo> packageCombos;
    @JsonIgnore
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL)
    private List<ImagePackage> imagePackages;


    public Package(String packageName, String packageDescription, Long packagePrice, Integer designatedHours, Integer workersNumber, Category category) {
        this.packageName = packageName;
        this.packageDescription = packageDescription;
        this.packagePrice = packagePrice;
        this.designatedHours = designatedHours;
        this.workersNumber = workersNumber;
        this.category = category;
    }
}
