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
    private Date orderDate;
    private String orderPlace;
    private Time orderTime;
    private Long commonUserId;
    private List<Long> packagesIds;
    private List<Long> combosIds;

    public Order toOrder(CommonUser commonUser) {
        Order order = new Order();
        order.setOrderDate(this.orderDate);
        order.setOrderPlace(this.orderPlace);
        order.setOrderTime(this.orderTime);
        // Estos valores serán establecidos en el servicio, por lo que no se incluyen aquí
        // order.setOrderTotalPayment(...);
        // order.setOrderTotalHours(...);
        // order.setOrderType(...);
        order.setCommonUser(commonUser); // El usuario se obtiene y pasa al método toOrder

        // Nota: Las listas de IDs para paquetes y combos se manejarán en el servicio,
        // no directamente en este método, ya que involucra la lógica de negocio adicional.

        return order;
    }
}
