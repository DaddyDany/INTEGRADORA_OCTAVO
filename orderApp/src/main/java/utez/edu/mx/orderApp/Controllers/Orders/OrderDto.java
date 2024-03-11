package utez.edu.mx.orderApp.Controllers.Orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Accounts.CommonUser;
import utez.edu.mx.orderApp.Models.Orders.Order;
import utez.edu.mx.orderApp.Models.Orders.OrderCombo;
import utez.edu.mx.orderApp.Models.Orders.OrderPackage;

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
