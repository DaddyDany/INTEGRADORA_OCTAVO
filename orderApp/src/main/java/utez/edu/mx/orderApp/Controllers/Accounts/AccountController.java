package utez.edu.mx.orderApp.Controllers.Accounts;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderApp.Models.Accounts.CommonUser;
import utez.edu.mx.orderApp.Models.Accounts.Worker;
import utez.edu.mx.orderApp.Services.AccountService;
import utez.edu.mx.orderApp.Utils.Response;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/create-common")
    public ResponseEntity<CommonUser> createCommonUserAccount(@RequestBody CommonUserDto commonUserDto) {
        try{
            Response createdAccount = accountService.createCommonUserAccount(commonUserDto);
            return new ResponseEntity(
                    createdAccount,
                    HttpStatus.CREATED
            );
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/create-admin")
    public ResponseEntity<AdministratorDto> createAdminAccount(@RequestBody AdministratorDto administratorDto) {
        try{
            Response createdAccount = accountService.createAdministratorAccount(administratorDto);
            return new ResponseEntity(
                    createdAccount,
                    HttpStatus.CREATED
            );
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/create-worker")
    public ResponseEntity<Worker> createAccount(@RequestBody WorkerDto workerDto) {
        try{
            Response createdAccount = accountService.createWorkerAccount(workerDto);
            return new ResponseEntity(
                    createdAccount,
                    HttpStatus.CREATED
            );
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
//        try{
//            accountService.deleteAccount(id);
//            return new ResponseEntity(
//                    this.accountService.deleteAccount(id),
//                    HttpStatus.OK
//            );
//        }catch (EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
