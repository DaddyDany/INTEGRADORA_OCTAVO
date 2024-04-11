package utez.edu.mx.orderapp.controllers.orders.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@Setter
@Getter
public class OrderInfoAdminDto extends OrderResponseDto {
    private String userName;
    private String userFirstLastName;
    private String userSecondLastName;
    private String userEmail;
    private String userCellphone;
    private List<String> packageNames;
    private List<String> workerNames;
}