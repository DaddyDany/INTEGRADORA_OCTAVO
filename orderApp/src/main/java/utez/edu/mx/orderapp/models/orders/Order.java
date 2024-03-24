package utez.edu.mx.orderapp.models.orders;

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
import utez.edu.mx.orderapp.models.accounts.CommonUser;

import java.sql.Time;
import java.util.Date;
import java.util.List;

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

    protected Order() {
        // Constructor for Hibernate's use only
    }
    public static class Builder {
        private final Order order = new Order();

        public Builder(Long orderId, Date orderDate, String orderState) {
            order.orderId = orderId;
            order.orderDate = orderDate;
            order.orderState = orderState;
        }

        public Builder withOrderPlace(String orderPlace) {
            order.orderPlace = orderPlace;
            return this;
        }

        public Builder withOrderTime(Time orderTime) {
            order.orderTime = orderTime;
            return this;
        }

        public Builder withOrderTotalPayment(Float orderTotalPayment) {
            order.orderTotalPayment = orderTotalPayment;
            return this;
        }

        public Builder withOrderPaymentState(Boolean orderPaymentState) {
            order.orderPaymentState = orderPaymentState;
            return this;
        }

        public Builder withOrderType(String orderType) {
            order.orderType = orderType;
            return this;
        }

        public Builder withOrderTotalHours(Integer orderTotalHours) {
            order.orderTotalHours = orderTotalHours;
            return this;
        }

        public Builder commonUser(CommonUser commonUser) {
            order.commonUser = commonUser;
            return this;
        }

        public Builder orderPackages(List<OrderPackage> orderPackages) {
            order.orderPackages = orderPackages;
            return this;
        }

        public Builder orderCombos(List<OrderCombo> orderCombos) {
            order.orderCombos = orderCombos;
            return this;
        }

        public Order build() {
            return order;
        }
    }
}
