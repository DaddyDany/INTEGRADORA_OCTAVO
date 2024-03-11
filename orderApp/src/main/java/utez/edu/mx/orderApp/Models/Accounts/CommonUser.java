package utez.edu.mx.orderApp.Models.Accounts;

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
import utez.edu.mx.orderApp.Models.Orders.Order;

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
    @ManyToOne
    @JoinColumn(name = "user_attributes_id", nullable = false)
    private UserAttribute userAttribute;
    @JsonIgnore
    @OneToMany(mappedBy = "commonUser")
    private List<Order> orders;
}
