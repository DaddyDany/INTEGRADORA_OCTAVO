package utez.edu.mx.orderapp.controllers.accounts.dtos;

import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.Worker;

@Getter
@Setter
public class WorkerGiveInfoDto {
    private String workerId;
    private String workerName;
    private String workerFirstLastName;
    private String workerSecondLastName;
    private String workerEmail;
    private String workerRfc;
    private String workerCellphone;
    private String workerSecurityNumber;
    private String workerSalary;
    private String accountStatus;
    private String workerProfilePicUrl;

    public WorkerGiveInfoDto(Worker worker) {
        this.workerName = worker.getWorkerName();
        this.workerFirstLastName = worker.getWorkerFirstLastName();
        this.workerSecondLastName = worker.getWorkerSecondLastName();
        this.workerEmail = worker.getWorkerEmail();
        this.workerRfc = worker.getWorkerRfc();
        this.workerCellphone = worker.getWorkerCellphone();
        this.workerProfilePicUrl = worker.getWorkerProfilePicUrl();
        this.accountStatus = worker.getAccountStatus();
    }
}
