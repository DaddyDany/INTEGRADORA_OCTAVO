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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ.,\\s]*$", message = "El campo nombre solo puede contener letras, puntos, comas, y caracteres acentuados")
    @Size(max = 20, message = "El nombre del combo debe tener máximo 20 caracteres")
    @Column(name = "combo_name")
    private String comboName;
    @NotNull(message = "La descripción no debe ser nula")
    @NotBlank(message = "La descripción no debe ir vacía")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ0-9;.,\\s()!?]*$", message = "El campo descripción solo puede contener letras, números, puntos, comas, paréntesis, signos de exclamación, signos de interrogación y caracteres acentuados")
    @Size(min = 50, max = 500, message = "La descripción del combo debe tener como mínimo 50 y como máximo 500 caracteres")
    @Column(name = "combo_description")
    private String comboDescription;
    @Column(name = "combo_img_url")
    private String comboImgUrl;
    @NotNull(message = "El precio no debe ser nulo")
    @Max(value = 20000, message = "El precio del combo no debe ser superios 20000")
    @Min(value = 1, message = "El precio no debe ser negativo")
    @Column(name = "combo_price")
    private Long comboPrice;
    @NotNull(message = "Las horas no deben ser nulas")
    @Max(value = 8, message = "Las horas designadas no deben superiores a 8")
    @Min(value = 1, message = "Las horas no deben ser negativas")
    @Column(name = "combo_designated_hours")
    private Integer comboDesignatedHours;
    @NotNull(message = "El número de trabajadores no debe ser nulo")
    @Max(value = 15, message = "El número de trabajadores no debe ser superior a 15")
    @Min(value = 1, message = "El número de trabajadores no debe ser negativo")
    @Column(name = "combo_workers_number")
    private Integer comboWorkersNumber;
    @JsonIgnore
    @OneToMany(mappedBy = "combo")
    private List<OrderCombo> orderCombos;
    @JsonIgnore
    @OneToMany(mappedBy = "combo",  cascade = CascadeType.ALL)
    private List<PackageCombo> packageCombos;
}
