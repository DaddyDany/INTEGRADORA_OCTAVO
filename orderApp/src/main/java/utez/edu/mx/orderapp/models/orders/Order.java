package utez.edu.mx.orderapp.models.orders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.reviews.Review;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "order_state")
    private String orderState;

    @Column(name = "order_place")
    private String orderPlace;

    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Column(name = "order_total_payment")
    private Float orderTotalPayment;

    @Column(name = "order_payment_state")
    private String orderPaymentState;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "order_total_hours")
    private Integer orderTotalHours;

    @ManyToOne
    @JoinColumn(name = "common_user_id")
    private CommonUser commonUser;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderCombo> orderCombos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderPackage> orderPackages = new ArrayList<>();

    @OneToMany(mappedBy = "order")
    private Set<WorkerOrder> workerOrders;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Review review;

    public Order() {
        // Constructor vacío necesario para JPA
    }

    public Order(Date orderDate, String orderPlace, LocalDateTime orderTime, CommonUser commonUser) {
        this.orderDate = orderDate;
        this.orderPlace = orderPlace;
        this.orderTime = orderTime;
        this.commonUser = commonUser;
    }

    public void addOrderPackage(OrderPackage orderPackage) {
    }
}
