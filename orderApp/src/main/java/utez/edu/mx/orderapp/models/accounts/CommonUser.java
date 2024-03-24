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
import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.orders.Order;

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
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_first_last_name")
    private String userFirstLastName;
    @Column(name = "user_second_last_name")
    private String userSecondLastName;
    @Column(name = "user_email")
    private String userEmail;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "user_cellphone")
    private String userCellphone;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @JsonIgnore
    @OneToMany(mappedBy = "commonUser")
    private List<Order> orders;
}
