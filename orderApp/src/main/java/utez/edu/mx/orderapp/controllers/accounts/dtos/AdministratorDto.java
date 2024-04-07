package utez.edu.mx.orderapp.controllers.accounts.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AdministratorDto {
    private String adminId;
    private String adminName;
    private String adminFirstLastName;
    private String adminSecondLastName;
    private String adminEmail;
    private String adminPassword;
    private String adminCellphone;
    private String adminSecurityNumber;
    private String adminSalary;
    private String accountStatus;
    private MultipartFile adminProfilePic;

}
