package utez.edu.mx.orderapp.controllers.orders.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> packageNames;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> comboNames;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> workerNames;
}