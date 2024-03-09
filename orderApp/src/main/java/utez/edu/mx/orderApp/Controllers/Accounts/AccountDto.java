package utez.edu.mx.orderApp.Controllers.Accounts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
    private String userCellphone;
    private String userEmail;
    private String userFirstLastName;
    private String userName;
    private String userPassword;
    private String userSecondLastName;
    private Long roleId;
}
