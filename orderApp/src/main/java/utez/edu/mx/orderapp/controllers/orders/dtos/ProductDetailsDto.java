package utez.edu.mx.orderapp.controllers.orders.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDetailsDto {
    private String orderName;
    private String orderDescription;
    private Integer designatedHours;
    private Integer workersNumber;
    private Long totalPrice;
}
