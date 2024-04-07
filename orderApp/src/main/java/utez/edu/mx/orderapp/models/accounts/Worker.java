package utez.edu.mx.orderapp.models.accounts;

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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.orders.WorkerOrder;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "workers")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worker_id")
    private Long workerId;

    @NotNull(message = "El nombre no debe ser nulo")
    @NotBlank(message = "El nombre no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ.,\\s]*$", message = "El campo solo puede contener letras, puntos, comas, y caracteres acentuados")
    @Column(name = "worker_name")
    private String workerName;

    @NotNull(message = "El apellido paterno no debe ser nulo")
    @NotBlank(message = "El apellido paterno no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ.,\\s]*$", message = "El campo solo puede contener letras, puntos, comas, y caracteres acentuados")
    @Column(name = "worker_first_last_name")
    private String workerFirstLastName;

    @NotNull(message = "El apellido materno no debe ser nulo")
    @NotBlank(message = "El apellido materno no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ.,\\s]*$", message = "El campo solo puede contener letras, puntos, comas, y caracteres acentuados")
    @Column(name = "worker_second_last_name")
    private String workerSecondLastName;

    @NotNull(message = "El correo no debe ser nulo")
    @NotBlank(message = "El correo no debe ir vacío")
    @Email(message = "Debe ser una dirección de correo electrónico válida")
    @Column(name = "worker_email", unique = true)
    private String workerEmail;

    @Column(name = "worker_password")
    private String workerPassword;

    @NotNull(message = "El telefono no debe ser nulo")
    @NotBlank(message = "El telefono no debe estar en blanco")
    @Size(min = 10, max = 10, message = "El teléfono debe tener exactamente 10 dígitos")
    @Pattern(regexp = "^\\d{10}$", message = "El teléfono solo debe contener dígitos")
    @Column(name = "worker_cellphone")
    private String workerCellphone;

    @NotNull(message = "El NSS no debe ser nulo")
    @NotBlank(message = "El NSS no debe estar en blanco")
    @Size(min = 11, max = 11, message = "El número de seguridad social debe tener 11 dígitos")
    @Column(name = "worker_security_number")
    private String workerSecurityNumber;

    @NotNull(message = "El salario no debe ser nulo")
    @Max(value = 20000, message = "El salario no debe ser superior a 50000, dudo que alguien aqui gane más que eso")
    @Column(name = "worker_salary")
    private Long workerSalary;

    @Column(name = "worker_profile_pic_url")
    private String workerProfilePicUrl;

    @NotNull(message = "El RFC no debe ser nulo")
    @NotBlank(message = "El RFC no debe estar en blanco")
    @Column(name = "worker_rfc")
    private String workerRfc;
    @Column(name = "account_status")
    private String accountStatus = "Sin confirmar";

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "confirmation_code_expiry")
    private LocalDateTime confirmationCodeExpiry;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToMany(mappedBy = "worker")
    private Set<WorkerOrder> workerOrders;
}
