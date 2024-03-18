package utez.edu.mx.orderApp.Controllers.Accounts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonUserDto {
    private String userName;
    private String userFirstLastName;
    private String userSecondLastName;
    private String userEmail;
    private String userPassword;
    private String userCellphone;
}
