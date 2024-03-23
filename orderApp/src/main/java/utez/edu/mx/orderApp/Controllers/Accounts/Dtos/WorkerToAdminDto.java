package utez.edu.mx.orderApp.Controllers.Accounts.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerToAdminDto {
    private String workerName;
    private String workerFirstLastName;
    private String workerSecondLastName;
    private String workerEmail;
    private String workerRfc;
    private String workerCellphone;

}
