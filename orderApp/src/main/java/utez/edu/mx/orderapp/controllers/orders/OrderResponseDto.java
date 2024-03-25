package utez.edu.mx.orderapp.controllers.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderResponseDto {
    private Long orderId;
    private Date orderDate;
    private String orderState;
    private String orderPlace;
    private Time orderTime;
    private Float orderTotalPayment;
    private String orderPaymentState;
    private String orderType;
    private Integer orderTotalHours;
    private Long commonUserId;
}
