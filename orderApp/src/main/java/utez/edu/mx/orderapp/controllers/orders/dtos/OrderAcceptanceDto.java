package utez.edu.mx.orderapp.controllers.orders.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderAcceptanceDto {
    private Long orderId;
    private List<Long> workerIds;

}
