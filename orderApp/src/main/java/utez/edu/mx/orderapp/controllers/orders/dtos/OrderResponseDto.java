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
    private String orderId;
    private String orderDate;
    private String orderState;
    private String orderPlace;
    private String orderTime;
    private String orderTotalPayment;
    private String orderPaymentState;
    private String orderType;
    private String orderTotalHours;
    private String orderTotalWorkers;
    private String commonUserId;
}
