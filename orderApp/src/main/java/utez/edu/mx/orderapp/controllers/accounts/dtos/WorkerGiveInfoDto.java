package utez.edu.mx.orderapp.controllers.accounts.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.Worker;
import utez.edu.mx.orderapp.utils.EncryptionService;

@Getter
@Setter
public class WorkerGiveInfoDto {
    private String workerId;
    private String workerName;
    private String workerFirstLastName;
    private String workerSecondLastName;
    private String workerEmail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workerRfc;
    private String workerCellphone;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workerSecurityNumber;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workerSalary;
    private String accountStatus;
    private String workerProfilePicUrl;

    public WorkerGiveInfoDto encryptFields(EncryptionService encryptionService) throws Exception {
        if (this.workerId != null) {
            this.workerId = encryptionService.encrypt(this.workerId);
        }
        if (this.workerName != null) {
            this.workerName = encryptionService.encrypt(this.workerName);
        }
        if (this.workerFirstLastName != null) {
            this.workerFirstLastName = encryptionService.encrypt(this.workerFirstLastName);
        }
        if (this.workerSecondLastName != null) {
            this.workerSecondLastName = encryptionService.encrypt(this.workerSecondLastName);
        }
        if (this.workerEmail != null) {
            this.workerEmail = encryptionService.encrypt(this.workerEmail);
        }
        if (this.workerCellphone != null) {
            this.workerCellphone = encryptionService.encrypt(this.workerCellphone);
        }
        if (this.accountStatus != null) {
            this.accountStatus = encryptionService.encrypt(this.accountStatus);
        }
        if (this.workerProfilePicUrl != null) {
            this.workerProfilePicUrl = encryptionService.encrypt(this.workerProfilePicUrl);
        }
        return this;
    }

    public WorkerGiveInfoDto(Worker worker) {
        this.workerId = String.valueOf(worker.getWorkerId());
        this.workerName = worker.getWorkerName();
        this.workerFirstLastName = worker.getWorkerFirstLastName();
        this.workerSecondLastName = worker.getWorkerSecondLastName();
        this.workerEmail = worker.getWorkerEmail();
        this.workerCellphone = worker.getWorkerCellphone();
        this.workerProfilePicUrl = worker.getWorkerProfilePicUrl();
        this.accountStatus = worker.getAccountStatus();
    }
}
