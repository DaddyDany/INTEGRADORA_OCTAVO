package utez.edu.mx.orderapp.models.combos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderapp.models.orders.OrderCombo;
import utez.edu.mx.orderapp.models.packages.PackageCombo;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "combos")
@NoArgsConstructor
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "combo_id")
    private Long comboId;
    @NotNull(message = "El nombre no debe ser nulo")
    @NotBlank(message = "El nombre no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ.,\\s]*$", message = "El campo solo puede contener letras, puntos, comas, y caracteres acentuados")
    @Column(name = "combo_name")
    private String comboName;
    @NotNull(message = "La descripción no debe ser nula")
    @NotBlank(message = "La descripción no debe ir vacía")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ.,\\s]*$", message = "El campo solo puede contener letras, puntos, comas, y caracteres acentuados")
    @Size(min = 50, max = 3000, message = "La descripción del servicio debe tener como mínimo 50 y como máximo 3000 caracteres")
    @Column(name = "combo_description")
    private String comboDescription;
    @Column(name = "combo_img_url")
    private String comboImgUrl;
    @NotNull(message = "El número de trabajadores no debe ser nulo")
    @Column(name = "combo_price")
    private Long comboPrice;
    @NotNull(message = "El número de trabajadores no debe ser nulo")
    @Column(name = "combo_designated_hours")
    private Integer comboDesignatedHours;
    @NotNull(message = "El número de trabajadores no debe ser nulo")
    @Column(name = "combo_workers_number")
    private Integer comboWorkersNumber;
    @JsonIgnore
    @OneToMany(mappedBy = "combo")
    private List<OrderCombo> orderCombos;
    @JsonIgnore
    @OneToMany(mappedBy = "combo",  cascade = CascadeType.ALL)
    private List<PackageCombo> packageCombos;
}
