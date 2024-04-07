package utez.edu.mx.orderapp.controllers.accounts.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class WorkerDto {
    private String workerId;
    private String workerName;
    private String workerFirstLastName;
    private String workerSecondLastName;
    private String workerEmail;
    private String workerPassword;
    private String workerCellphone;
    private String workerSecurityNumber;
    private String workerSalary;
    private String workerRfc;
    private MultipartFile workerProfilePic;
}
