package utez.edu.mx.orderapp.controllers.orders.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private LocalDateTime orderTime;
    private Float orderTotalPayment;
    private String orderPaymentState;
    private String orderType;
    private Integer orderTotalHours;
    private Long commonUserId;
}
