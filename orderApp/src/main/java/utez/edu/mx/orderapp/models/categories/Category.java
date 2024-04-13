package utez.edu.mx.orderapp.models.categories;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import utez.edu.mx.orderapp.models.packages.Package;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "services")
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;
    @NotNull(message = "El nombre no debe ser nulo")
    @NotBlank(message = "El nombre no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ.,\\s]*$", message = "El campo nombre solo puede contener letras, puntos, comas, y caracteres acentuados")
    @Column(name = "service_name")
    private String serviceName;
    @NotNull(message = "La descripción no debe ser nula")
    @NotBlank(message = "La descripción no debe ir vacía")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ.,\\s()!?]*$", message = "El campo descripción solo puede contener letras, puntos, comas, paréntesis, signos de exclamación, signos de interrogación y caracteres acentuados")
    @Size(min = 50, max = 3000, message = "La descripción del servicio debe tener como mínimo 50 y como máximo 3000 caracteres")
    @Column(name = "service_description")
    private String serviceDescription;
    @NotNull(message = "La frase no debe ser nula")
    @NotBlank(message = "La frase no debe ir vacía")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ.,\\s()!?]*$", message = "El campo de la frase solo puede contener letras, puntos, comas, paréntesis, signos de exclamación, signos de interrogación y caracteres acentuados")
    @Size(min = 10, max = 60, message = "La frase del servicio debe tener como mínimo 10 y como máximo 60 caracteres")
    @Column(name = "service_quote")
    private String serviceQuote;
    @Column(name = "service_state")
    private Boolean serviceState;
    @Column(name = "service_img_url")
    private String serviceImageUrl;
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Package> packages;
}
