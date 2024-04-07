package utez.edu.mx.orderapp.controllers.accounts.dtos;

import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.Administrator;

@Getter
@Setter
public class AdminGiveInfoDto {
    private String adminId;
    private String adminName;
    private String adminFirstLastName;
    private String adminSecondLastName;
    private String adminEmail;
    private String adminCellphone;
    private String adminSecurityNumber;
    private String adminProfilePicUrl;
    private String adminSalary;
    private String accountStatus;

    public AdminGiveInfoDto(Administrator administrator) {
        this.adminName = administrator.getAdminName();
        this.adminFirstLastName = administrator.getAdminFirstLastName();
        this.adminSecondLastName = administrator.getAdminSecondLastName();
        this.adminEmail = administrator.getAdminEmail();
        this.adminCellphone = administrator.getAdminCellphone();
        this.adminSecurityNumber = administrator.getAdminSecurityNumber().toString();
        this.adminProfilePicUrl = administrator.getAdminProfilePicUrl();
        this.adminSalary = administrator.getAdminSalary().toString();
    }
}
