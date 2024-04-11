package utez.edu.mx.orderapp.controllers.accounts.dtos;

import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.Administrator;
import utez.edu.mx.orderapp.utils.EncryptionService;

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

    public AdminGiveInfoDto encryptFields(EncryptionService encryptionService) throws Exception {
        if (this.adminId != null) {
            this.adminId = encryptionService.encrypt(this.adminId);
        }
        if (this.adminName != null) {
            this.adminName = encryptionService.encrypt(this.adminName);
        }
        if (this.adminFirstLastName != null) {
            this.adminFirstLastName = encryptionService.encrypt(this.adminFirstLastName);
        }
        if (this.adminSecondLastName != null) {
            this.adminSecondLastName = encryptionService.encrypt(this.adminSecondLastName);
        }
        if (this.adminEmail != null) {
            this.adminEmail = encryptionService.encrypt(this.adminEmail);
        }
        if (this.adminCellphone != null) {
            this.adminCellphone = encryptionService.encrypt(this.adminCellphone);
        }
        if (this.adminSecurityNumber != null) {
            this.adminSecurityNumber = encryptionService.encrypt(this.adminSecurityNumber);
        }
        if (this.adminProfilePicUrl != null) {
            this.adminProfilePicUrl = encryptionService.encrypt(this.adminProfilePicUrl);
        }
        if (this.adminSalary != null) {
            this.adminSalary = encryptionService.encrypt(this.adminSalary);
        }
        if (this.accountStatus != null) {
            this.accountStatus = encryptionService.encrypt(this.accountStatus);
        }
        return this;
    }


    public AdminGiveInfoDto(Administrator administrator) {
        this.adminId = String.valueOf(administrator.getAdminId());
        this.adminName = administrator.getAdminName();
        this.adminFirstLastName = administrator.getAdminFirstLastName();
        this.adminSecondLastName = administrator.getAdminSecondLastName();
        this.adminEmail = administrator.getAdminEmail();
        this.adminCellphone = administrator.getAdminCellphone();
        this.adminSecurityNumber = administrator.getAdminSecurityNumber();
        this.adminProfilePicUrl = administrator.getAdminProfilePicUrl();
        this.adminSalary = administrator.getAdminSalary().toString();
    }
}
