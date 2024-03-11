package utez.edu.mx.orderApp.Models.Orders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Accounts.CommonUser;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
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
    private Time orderTime;
    @Column(name = "order_total_payment")
    private Float orderTotalPayment;
    @Column(name = "order_payment_state")
    private Boolean orderPaymentState;
    @Column(name = "order_type")
    private String orderType;
    @Column(name = "order_total_hours")
    private Integer orderTotalHours;
    @ManyToOne
    @JoinColumn(name = "common_user_id")
    private CommonUser commonUser;
    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderCombo> orderCombos;
    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderPackage> orderPackages;



    public Order(Long orderId, Date orderDate, String orderState, String orderPlace, Time orderTime, Float orderTotalPayment, Boolean orderPaymentState, String orderType, Integer orderTotalHours, CommonUser commonUser, List<OrderPackage> orderPackages, List<OrderCombo> orderCombos) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderState = orderState;
        this.orderPlace = orderPlace;
        this.orderTime = orderTime;
        this.orderTotalPayment = orderTotalPayment;
        this.orderPaymentState = orderPaymentState;
        this.orderType = orderType;
        this.orderTotalHours = orderTotalHours;
        this.commonUser = commonUser;
        this.orderPackages = orderPackages;
        this.orderCombos = orderCombos;
    }
}
