package utez.edu.mx.orderapp.controllers.accounts.dtos;

import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.Worker;

@Getter
@Setter
public class WorkerGiveInfoDto {
    private Long workerId;
    private String workerName;
    private String workerFirstLastName;
    private String workerSecondLastName;
    private String workerEmail;
    private String workerRfc;
    private String workerCellphone;
    private Integer workerSecurityNumber;
    private Float workerSalary;
    private String workerProfilePicUrl;

    public WorkerGiveInfoDto(Worker worker) {
        this.workerId = worker.getWorkerId();
        this.workerName = worker.getWorkerName();
        this.workerFirstLastName = worker.getWorkerFirstLastName();
        this.workerSecondLastName = worker.getWorkerSecondLastName();
        this.workerEmail = worker.getWorkerEmail();
        this.workerRfc = worker.getWorkerRfc();
        this.workerCellphone = worker.getWorkerCellphone();
        this.workerSecurityNumber = worker.getWorkerSecurityNumber();
        this.workerSalary = worker.getWorkerSalary();
        this.workerProfilePicUrl = worker.getWorkerProfilePicUrl();
    }
}
