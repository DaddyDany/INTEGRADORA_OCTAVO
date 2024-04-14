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
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.reviews.Review;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    @NotNull(message = "La fecha del pedido no puede ser nula.")
    @FutureOrPresent(message = "La fecha del pedido no puede ser menor que la fecha actual.")
    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "order_state")
    private String orderState;

    @NotNull(message = "El nombre no debe ser nulo")
    @NotBlank(message = "El nombre no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ.,\\s]*$", message = "El campo solo puede contener letras, puntos, comas, y caracteres acentuados")
    @Column(name = "order_place")
    private String orderPlace;

    @NotNull(message = "La hora del pedido no puede ser nula.")
    @Column(name = "order_time")
    private LocalTime orderTime;

    @Column(name = "order_total_payment")
    private Float orderTotalPayment;

    @Column(name = "order_payment_state")
    private String orderPaymentState;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "session_id", unique = true)
    private String sessionId;

    @Column(name = "order_total_hours")
    private Integer orderTotalHours;

    @Column(name = "order_total_workers")
    private Integer orderTotalWokers;

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

    @OneToOne(mappedBy = "order")
    private Review review;

    public Order() {
        // Constructor vacío necesario para JPA
    }

    public Order(LocalDate orderDate, String orderPlace, LocalTime orderTime, CommonUser commonUser) {
        this.orderDate = orderDate;
        this.orderPlace = orderPlace;
        this.orderTime = orderTime;
        this.commonUser = commonUser;
    }
}
