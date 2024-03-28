package utez.edu.mx.orderapp.controllers.accounts;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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

import java.io.IOException;
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
    public ResponseEntity<Response<Long>> createCommonUserAccount(@ModelAttribute CommonUserDto commonUserDto){
        Response<Long> response = accountService.createCommonUserAccount(commonUserDto);
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
    public ResponseEntity<Response<Long>> createAdminAccount(@ModelAttribute AdministratorDto administratorDto){
        Response<Long> response = accountService.createAdministratorAccount(administratorDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/update-admin/info/{adminId}")
    public ResponseEntity<Response<Long>> updateAdmin(@PathVariable Long adminId, @RequestBody AdministratorDto administratorDto) {
        Response<Long> response = accountService.updateAdminInfo(adminId, administratorDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/update-admin/profile-pic/{adminId}")
    public ResponseEntity<Response<String>> updateAdminProfilePic(@PathVariable Long adminId, @RequestParam("profilePic") MultipartFile profilePic) throws IOException {
        Response<String> response = accountService.updateAdminProfilePic(adminId , profilePic);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
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
    public ResponseEntity<Response<Long>> createAccount(@ModelAttribute WorkerDto workerDto) throws IOException {
        Response<Long> response = accountService.createWorkerAccount(workerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update-worker/info/{workerId}")
    public ResponseEntity<Response<Long>> updateWorker(@PathVariable Long workerId, @RequestBody WorkerDto workerDto) {
        Response<Long> response = accountService.updateWorkerInfo(workerId, workerDto);
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
