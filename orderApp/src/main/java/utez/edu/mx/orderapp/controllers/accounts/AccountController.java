package utez.edu.mx.orderapp.controllers.accounts;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderapp.controllers.accounts.dtos.AdministratorDto;
import utez.edu.mx.orderapp.controllers.accounts.dtos.AdminGiveInfoDto;
import utez.edu.mx.orderapp.controllers.accounts.dtos.CommonUserDto;
import utez.edu.mx.orderapp.controllers.accounts.dtos.WorkerDto;
import utez.edu.mx.orderapp.controllers.accounts.dtos.WorkerGiveInfoDto;
import utez.edu.mx.orderapp.models.accounts.Administrator;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.accounts.Worker;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.services.accounts.AccountService;
import utez.edu.mx.orderapp.utils.Response;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {
    private final AccountService accountService;
    private final CommonUserRepository commonUserRepository;
    @Autowired
    public AccountController(AccountService accountService, CommonUserRepository commonUserRepository){
        this.accountService = accountService;
        this.commonUserRepository = commonUserRepository;
    }
    @PostMapping("/create-common")
    public ResponseEntity<CommonUser> createCommonUserAccount(@RequestBody CommonUserDto commonUserDto) {
        CommonUser createdAccount = accountService.createCommonUserAccount(commonUserDto).getData();
        Response<CommonUser> responseBody = new Response<>(createdAccount, false, HttpStatus.CREATED.value(), "Cuenta creada exitosamente");
        return new ResponseEntity<>(responseBody.getData(), HttpStatus.CREATED);
    }

    @PostMapping("/confirm-account")
    public ResponseEntity<?> confirmAccount(@RequestParam("token") String token) {
        CommonUser user = commonUserRepository.findByConfirmationCode(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired confirmation token"));

        if (LocalDateTime.now().isBefore(user.getConfirmationCodeExpiry())) {
            user.setAccountStatus("Confirmada");
            commonUserRepository.save(user);
            return ResponseEntity.ok("Account successfully confirmed.");
        } else {
            return ResponseEntity.badRequest().body("Confirmation token is invalid or expired.");
        }
    }

    @PostMapping("/create-admin")
    public ResponseEntity<Administrator> createAdminAccount(@RequestBody AdministratorDto administratorDto) {
        try{
            Response<Administrator> createdAccount = accountService.createAdministratorAccount(administratorDto);
            return new ResponseEntity<>(createdAccount.getData(), HttpStatus.CREATED);
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
            Response<Worker> createdAccount = accountService.createWorkerAccount(workerDto);
            return new ResponseEntity<>(
                    createdAccount.getData(),
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
}
