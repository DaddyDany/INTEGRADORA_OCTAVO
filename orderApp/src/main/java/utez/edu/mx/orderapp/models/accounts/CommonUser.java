package utez.edu.mx.orderapp.models.accounts;

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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.orders.Order;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "common_users")
public class CommonUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "common_user_id")
    private Long commonUserId;

    @NotNull(message = "El nombre no debe ser nulo")
    @NotBlank(message = "El nombre no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]*$", message = "El campo nombre solo puede contener letras y caracteres acentuados")
    @Column(name = "user_name")
    private String userName;

    @NotNull(message = "El apellido paterno no debe ser nulo")
    @NotBlank(message = "El apellido paterno no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]*$", message = "El campo apellido paterno solo puede contener letras y caracteres acentuados")
    @Column(name = "user_first_last_name")
    private String userFirstLastName;

    @NotNull(message = "El apellido materno no debe ser nulo")
    @NotBlank(message = "El apellido materno no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]*$", message = "El campo apellido materno solo puede contener letras, puntos, comas, y caracteres acentuados")
    @Column(name = "user_second_last_name")
    private String userSecondLastName;

    @NotNull(message = "El correo no debe ser nulo")
    @NotBlank(message = "El correo no debe ir vacío")
    @Email(message = "Debe ser una dirección de correo electrónico válida")
    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_password")
    private String userPassword;

    @NotNull(message = "El telefono no debe ser nulo")
    @NotBlank(message = "El telefono no debe estar en blanco")
    @Size(min = 10, max = 10, message = "El teléfono debe tener exactamente 10 dígitos")
    @Pattern(regexp = "^\\d{10}$", message = "El teléfono solo debe contener dígitos")
    @Column(name = "user_cellphone")
    private String userCellphone;
    @Column(name = "user_profile_pic_url")
    private String userProfilePicUrl;

    @Column(name = "account_status")
    private String accountStatus = "Sin confirmar";

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "confirmation_code_expiry")
    private LocalDateTime confirmationCodeExpiry;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @JsonIgnore
    @OneToMany(mappedBy = "commonUser")
    private List<Order> orders;
}
