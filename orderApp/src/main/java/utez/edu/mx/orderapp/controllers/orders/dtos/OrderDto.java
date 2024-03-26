package utez.edu.mx.orderapp.controllers.orders.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.orders.Order;
import utez.edu.mx.orderapp.models.orders.OrderCombo;
import utez.edu.mx.orderapp.models.orders.OrderPackage;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDto {
    private Date orderDate;
    private String orderPlace;
    private LocalDateTime orderTime;
    private Long commonUserId;
    private List<Long> packagesIds;
    private List<Long> combosIds;

    public Order toOrder(CommonUser commonUser) {
        Order order = new Order();
        order.setOrderDate(this.orderDate);
        order.setOrderPlace(this.orderPlace);
        order.setOrderTime(this.orderTime);
        order.setCommonUser(commonUser);
        return order;
    }
}
