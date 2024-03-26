package utez.edu.mx.orderapp.controllers.orders.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class OrderInfoAdminDto extends OrderResponseDto {
    private String userName;
    private String userFirstLastName;
    private String userSecondLastName;
    private String userEmail;
    private String userCellphone;
}