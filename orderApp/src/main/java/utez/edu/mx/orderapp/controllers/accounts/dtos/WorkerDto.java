package utez.edu.mx.orderapp.controllers.accounts.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class WorkerDto {
    private String workerName;
    private String workerFirstLastName;
    private String workerSecondLastName;
    private String workerEmail;
    private String workerPassword;
    private String workerCellphone;
    private Integer workerSecurityNumber;
    private Float workerSalary;
    private String workerRfc;
    private MultipartFile workerProfilePic;
}
