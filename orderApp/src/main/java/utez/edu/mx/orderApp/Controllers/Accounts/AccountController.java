package utez.edu.mx.orderApp.Controllers.Accounts;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderApp.Controllers.Accounts.Dtos.AdministratorDto;
import utez.edu.mx.orderApp.Controllers.Accounts.Dtos.AdminGiveInfoDto;
import utez.edu.mx.orderApp.Controllers.Accounts.Dtos.CommonUserDto;
import utez.edu.mx.orderApp.Controllers.Accounts.Dtos.WorkerDto;
import utez.edu.mx.orderApp.Controllers.Accounts.Dtos.WorkerGiveInfoDto;
import utez.edu.mx.orderApp.Models.Accounts.CommonUser;
import utez.edu.mx.orderApp.Models.Accounts.Worker;
import utez.edu.mx.orderApp.Services.AccountService;
import utez.edu.mx.orderApp.Utils.Response;

import java.util.List;

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

    @GetMapping("/administrators")
    public ResponseEntity<List<AdminGiveInfoDto>> getAllAdministrators() {
        try {
            List<AdminGiveInfoDto> administrators = accountService.findAllAdministrators();
            return new ResponseEntity<>(administrators, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

    @GetMapping("/workers")
    public ResponseEntity<List<WorkerGiveInfoDto>> getAllWorkers() {
        try {
            List<WorkerGiveInfoDto> workers = accountService.findAllWorkers();
            return new ResponseEntity<>(workers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Rol no encontrado"))
                .getAuthority();

        Object userProfile = accountService.getLoggedUserProfile(username, role);
        return ResponseEntity.ok(userProfile);
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
