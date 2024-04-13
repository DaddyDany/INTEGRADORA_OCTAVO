package utez.edu.mx.orderapp.controllers.orders.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.orders.Order;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDto {
    private String orderId;
    private LocalDate orderDate;
    private String orderPlace;
    private LocalTime orderTime;
    private Long commonUserId;
    private String sessionId;
    private List<Long> packagesIds = new ArrayList<>();

    private List<Long> combosIds = new ArrayList<>();

    public Order toOrder(CommonUser commonUser) {
        Order order = new Order();
        order.setOrderDate(this.orderDate);
        order.setOrderPlace(this.orderPlace);
        order.setOrderTime(this.orderTime);
        order.setCommonUser(commonUser);
        return order;
    }
}
