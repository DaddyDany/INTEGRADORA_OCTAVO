package utez.edu.mx.orderapp.models.accounts;

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

@Getter
@Setter
@Entity
@Table(name = "workers")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worker_id")
    private Long workerId;
    @Column(name = "worker_name")
    private String workerName;
    @Column(name = "worker_first_last_name")
    private String workerFirstLastName;
    @Column(name = "worker_second_last_name")
    private String workerSecondLastName;
    @Column(name = "worker_email")
    private String workerEmail;
    @Column(name = "worker_password")
    private String workerPassword;
    @Column(name = "worker_cellphone")
    private String workerCellphone;
    @Column(name = "worker_security_number")
    private Integer workerSecurityNumber;
    @Column(name = "worker_salary")
    private Integer workerSalary;
    @Column(name = "worker_rfc")
    private String workerRfc;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
