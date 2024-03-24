package utez.edu.mx.orderapp.controllers.accounts.dtos;

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
