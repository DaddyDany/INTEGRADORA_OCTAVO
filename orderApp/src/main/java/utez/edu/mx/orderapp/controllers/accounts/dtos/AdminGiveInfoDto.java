package utez.edu.mx.orderapp.controllers.accounts.dtos;

import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.Administrator;

@Getter
@Setter
public class AdminGiveInfoDto {
    private Long adminId;
    private String adminName;
    private String adminFirstLastName;
    private String adminSecondLastName;
    private String adminEmail;
    private String adminCellphone;
    private String adminSecurityNumber;

    public AdminGiveInfoDto(Administrator administrator) {
        this.adminId = administrator.getAdminId();
        this.adminName = administrator.getAdminName();
        this.adminFirstLastName = administrator.getAdminFirstLastName();
        this.adminSecondLastName = administrator.getAdminSecondLastName();
        this.adminEmail = administrator.getAdminEmail();
        this.adminCellphone = administrator.getAdminCellphone();
        this.adminSecurityNumber = administrator.getAdminSecurityNumber().toString();
    }
}
