package utez.edu.mx.orderApp.Controllers.Accounts.Dtos;

import lombok.Getter;
import lombok.Setter;

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
    private Integer workerSalary;
    private String workerRfc;
}
