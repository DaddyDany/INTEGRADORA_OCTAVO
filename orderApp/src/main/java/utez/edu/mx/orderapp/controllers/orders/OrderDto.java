package utez.edu.mx.orderapp.controllers.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.orders.Order;
import utez.edu.mx.orderapp.models.orders.OrderCombo;
import utez.edu.mx.orderapp.models.orders.OrderPackage;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDto {
    private Long orderId;
    private Date orderDate;
    private String orderState;
    private String orderPlace;
    private Time orderTime;
    private Float orderTotalPayment;
    private Boolean orderPaymentState;
    private String orderType;
    private Integer orderTotalHours;
    private CommonUser commonUser;
    private List<OrderPackage> orderPackages;
    private List<OrderCombo> orderCombos;
    public Order getOrder(){
        return new Order(
                getOrderId(),
                getOrderDate(),
                getOrderState(),
                getOrderPlace(),
                getOrderTime(),
                getOrderTotalPayment(),
                getOrderPaymentState(),
                getOrderType(),
                getOrderTotalHours(),
                getCommonUser(),
                getOrderPackages(),
                getOrderCombos()
        );
    }
}
