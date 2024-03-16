package utez.edu.mx.orderApp.Controllers.Accounts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDto {
    private String adminName;
    private String adminFirstLastName;
    private String adminSecondLastName;
    private String adminEmail;
    private String adminPassword;
    private String adminCellphone;
    private Integer adminSecurityNumber;
    private Integer adminSalary;
}
