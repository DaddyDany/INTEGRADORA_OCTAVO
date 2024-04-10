package utez.edu.mx.orderapp.controllers.accounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.orderapp.controllers.accounts.dtos.AdminGiveInfoDto;
import utez.edu.mx.orderapp.controllers.accounts.dtos.CommonUserDto;
import utez.edu.mx.orderapp.controllers.accounts.dtos.WorkerGiveInfoDto;
import utez.edu.mx.orderapp.models.accounts.Administrator;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.accounts.Worker;
import utez.edu.mx.orderapp.repositories.accounts.AdministratorRepository;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.repositories.accounts.WorkerRepository;
import utez.edu.mx.orderapp.services.accounts.AccountService;
import utez.edu.mx.orderapp.utils.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {
    private final AccountService accountService;
    private final CommonUserRepository commonUserRepository;
    private final WorkerRepository workerRepository;
    private final AdministratorRepository administratorRepository;
    @Autowired
    public AccountController(AccountService accountService, CommonUserRepository commonUserRepository, AdministratorRepository administratorRepository, WorkerRepository workerRepository){
        this.accountService = accountService;
        this.commonUserRepository = commonUserRepository;
        this.administratorRepository = administratorRepository;
        this.workerRepository = workerRepository;
    }
    @PostMapping("/create-common")
    public ResponseEntity<Response<String>> createCommonUserAccount(@RequestPart("data") String encryptedData, @RequestParam(value = "userProfilePic", required = false) MultipartFile userProfilePic) throws Exception{
        Response<String> response = accountService.createCommonUserAccount(encryptedData, userProfilePic);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/update/info/{userId}")
    public ResponseEntity<Response<Long>> updateUserInfo(@PathVariable Long userId, @RequestBody CommonUserDto commonUserDto) {
        Response<Long> response = accountService.updateCommonUserInfo(userId, commonUserDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/update/profile-pic/{userId}")
    public ResponseEntity<Response<String>> updateUserProfilePic(@PathVariable Long userId, @RequestParam("profilePic") MultipartFile profilePic) throws IOException {
        Response<String> response = accountService.updateCommonUserProfilePic(userId, profilePic);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/create-admin")
    public ResponseEntity<Response<String>> createAdminAccount(@RequestPart("data") String encryptedData, @RequestParam(value = "adminProfilePic", required = false) MultipartFile adminProfilePic) throws Exception{
        Response<String> response = accountService.createAdministratorAccount(encryptedData, adminProfilePic);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-admin")
    public ResponseEntity<Response<String>> deleteAdminAccount(@RequestBody String encryptedData) throws Exception{
        Response<String> response = accountService.deleteAdmin(encryptedData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-worker")
    public ResponseEntity<Response<String>> deleteWorkerAccount(@RequestBody String encryptedData) throws Exception{
        Response<String> response = accountService.deleteWorker(encryptedData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-admin")
    public ResponseEntity<Response<String>> updateAdmin(@RequestPart("data") String encryptedData) throws Exception {
        Response<String> response = accountService.updateAdminInfo(encryptedData);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/update-admin/profile-pic/{adminId}")
    public ResponseEntity<Response<String>> updateAdminProfilePic(@PathVariable Long adminId, @RequestParam("profilePic") MultipartFile profilePic) throws IOException {
        Response<String> response = accountService.updateAdminProfilePic(adminId , profilePic);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/confirm-account")
    public ResponseEntity<String> confirmAccount(@RequestParam("token") String token) {
        CommonUser user = commonUserRepository.findByConfirmationCode(token)
                .orElseThrow(() -> new RuntimeException("Token no valido o expirado"));

        if (LocalDateTime.now().isBefore(user.getConfirmationCodeExpiry())) {
            user.setAccountStatus("Confirmada");
            commonUserRepository.save(user);
            return ResponseEntity.ok("Cuenta confirmada con exito.");
        } else {
            return ResponseEntity.badRequest().body("Token no valido o expirado");
        }
    }

    @PostMapping("/confirm-worker-account")
    public ResponseEntity<String> confirmWorkerAccount(@RequestParam("token") String token) {
        Worker worker = workerRepository.findByConfirmationCode(token)
                .orElseThrow(() -> new RuntimeException("Token no valido o expirado"));

        if (LocalDateTime.now().isBefore(worker.getConfirmationCodeExpiry())) {
            worker.setAccountStatus("Confirmada");
            workerRepository.save(worker);
            return ResponseEntity.ok("Cuenta de trabajador confirmada con exito.");
        } else {
            return ResponseEntity.badRequest().body("Token no valido o expirado");
        }
    }

    @PostMapping("/confirm-admin-account")
    public ResponseEntity<String> confirmAdminAccount(@RequestParam("token") String token) {
        Administrator admin = administratorRepository.findByConfirmationCode(token)
                .orElseThrow(() -> new RuntimeException("Token no valido o expirado"));

        if (LocalDateTime.now().isBefore(admin.getConfirmationCodeExpiry())) {
            admin.setAccountStatus("Confirmada");
            administratorRepository.save(admin);
            return ResponseEntity.ok("Cuenta de administrador confirmada con éxito.");
        } else {
            return ResponseEntity.badRequest().body("Token no valido o expirado");
        }
    }

    @PostMapping("/create-worker")
    public ResponseEntity<Response<Long>> createWorkerAccount(
            @RequestPart("data") String encryptedData,
            @RequestParam(value = "workerProfilePic", required = false) MultipartFile workerProfilePic) throws Exception {
        Response<Long> response = accountService.createWorkerAccount(encryptedData, workerProfilePic);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update-worker")
    public ResponseEntity<Response<String>> updateWorker(@RequestPart("data") String encryptedData) throws Exception{
        Response<String> response = accountService.updateWorkerInfo(encryptedData);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }


    @PostMapping("/update-worker/profile-pic/{workerId}")
    public ResponseEntity<Response<String>> updateWorkerProfilePic(@PathVariable Long workerId, @RequestParam("profilePic") MultipartFile profilePic) throws IOException {
        Response<String> response = accountService.updateWorkerProfilePic(workerId , profilePic);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
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

    @GetMapping("/administrators")
    public ResponseEntity<List<AdminGiveInfoDto>> getAllAdministrators() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAdminId = authentication.getName();
        List<AdminGiveInfoDto> administrators = accountService.findAllAdministratorsExcludeLogged(currentAdminId);
        return ResponseEntity.ok(administrators);
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
