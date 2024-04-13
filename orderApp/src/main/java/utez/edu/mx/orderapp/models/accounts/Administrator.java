package utez.edu.mx.orderapp.models.accounts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "administrators")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @NotNull(message = "El nombre no debe ser nulo")
    @NotBlank(message = "El nombre no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]*$", message = "El campo nombre solo puede contener letras y caracteres acentuados")
    @Column(name = "admin_name")
    private String adminName;

    @NotNull(message = "El apellido paterno no debe ser nulo")
    @NotBlank(message = "El apellido paterno no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]*$", message = "El campo apellido paterno solo puede contener letras y caracteres acentuados")
    @Column(name = "admin_first_last_name")
    private String adminFirstLastName;

    @NotNull(message = "El apellido materno no debe ser nulo")
    @NotBlank(message = "El apellido materno no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]*$", message = "El campo apellido materno solo puede contener letras y caracteres acentuados")
    @Column(name = "admin_second_last_name")
    private String adminSecondLastName;

    @NotNull(message = "El correo no debe ser nulo")
    @NotBlank(message = "El correo no debe ir vacío")
    @Email(message = "Debe ser una dirección de correo electrónico válida")
    @Column(name = "admin_email", unique = true)
    private String adminEmail;

    @Column(name = "admin_password")
    private String adminPassword;

    @NotNull(message = "El telefono no debe ser nulo")
    @NotBlank(message = "El telefono no debe estar en blanco")
    @Size(min = 10, max = 10, message = "El teléfono debe tener exactamente 10 dígitos")
    @Pattern(regexp = "^\\d{10}$", message = "El teléfono solo debe contener dígitos")
    @Column(name = "admin_cellphone")
    private String adminCellphone;

    @Column(name = "admin_profile_pic_url")
    private String adminProfilePicUrl;

    @NotNull(message = "El NSS no debe ser nulo")
    @NotBlank(message = "El NSS no debe estar en blanco")
    @Size(min = 11, max = 11, message = "El número de seguridad social debe tener 11 dígitos")
    @Column(name = "admin_security_number", unique = true)
    private String adminSecurityNumber;

    @NotNull(message = "El salario no debe ser nulo")
    @Max(value = 50000, message = "El salario no debe ser superior a 50000, dudo que alguien aqui gane más que eso")
    @Min(value = 0, message = "El salario del administrador no debe ser negativo")
    @Column(name = "admin_salary")
    private Long adminSalary;

    @Column(name = "account_status")
    private String accountStatus = "Sin confirmar";

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "confirmation_code_expiry")
    private LocalDateTime confirmationCodeExpiry;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
