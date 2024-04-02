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
@Table(name = "administrators")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;
    @Column(name = "admin_name")
    private String adminName;
    @Column(name = "admin_first_last_name")
    private String adminFirstLastName;
    @Column(name = "admin_second_last_name")
    private String adminSecondLastName;
    @Column(name = "admin_email")
    private String adminEmail;
    @Column(name = "admin_password")
    private String adminPassword;
    @Column(name = "admin_cellphone")
    private String adminCellphone;
    @Column(name = "admin_profile_pic_url")
    private String adminProfilePicUrl;
    @Column(name = "admin_security_number")
    private String adminSecurityNumber;
    @Column(name = "admin_salary")
    private Integer adminSalary;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
