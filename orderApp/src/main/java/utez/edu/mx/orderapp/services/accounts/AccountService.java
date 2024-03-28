package utez.edu.mx.orderapp.services.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.orderapp.controllers.accounts.dtos.AdministratorDto;
import utez.edu.mx.orderapp.controllers.accounts.dtos.AdminGiveInfoDto;
import utez.edu.mx.orderapp.controllers.accounts.dtos.CommonUserDto;
import utez.edu.mx.orderapp.controllers.accounts.dtos.CommonUserGiveInfoDto;
import utez.edu.mx.orderapp.controllers.accounts.dtos.WorkerDto;
import utez.edu.mx.orderapp.controllers.accounts.dtos.WorkerGiveInfoDto;
import utez.edu.mx.orderapp.firebaseintegrations.FirebaseStorageService;
import utez.edu.mx.orderapp.models.accounts.Administrator;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.accounts.Role;
import utez.edu.mx.orderapp.models.accounts.Worker;
import utez.edu.mx.orderapp.repositories.accounts.AdministratorRepository;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.repositories.accounts.RoleRepository;
import utez.edu.mx.orderapp.repositories.accounts.WorkerRepository;
import utez.edu.mx.orderapp.services.emails.EmailService;
import utez.edu.mx.orderapp.utils.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class AccountService {
    private final RoleRepository roleRepository;
    private final CommonUserRepository commonUserRepository;
    private final WorkerRepository workerRepository;
    private final AdministratorRepository administratorRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final FirebaseStorageService firebaseStorageService;
    private static final String ROLE_WORKER = "WORKER";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_COMMON_USER = "COMMON_USER";
    private static final String ROLE_NOT_FOUND = "Rol no encontrado";


    @Autowired
    public AccountService(RoleRepository roleRepository, CommonUserRepository commonUserRepository, WorkerRepository workerRepository, AdministratorRepository administratorRepository, PasswordEncoder passwordEncoder, EmailService emailService, FirebaseStorageService firebaseStorageService){
        this.roleRepository = roleRepository;
        this.commonUserRepository = commonUserRepository;
        this.workerRepository = workerRepository;
        this.administratorRepository = administratorRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Long> createCommonUserAccount(CommonUserDto commonUserDto) {
        try {
            CommonUser commonUser = new CommonUser();
            commonUser.setUserCellphone(commonUserDto.getUserCellphone());
            commonUser.setUserEmail(commonUserDto.getUserEmail());
            commonUser.setUserFirstLastName(commonUserDto.getUserFirstLastName());
            commonUser.setUserName(commonUserDto.getUserName());
            commonUser.setUserPassword(passwordEncoder.encode(commonUserDto.getUserPassword()));
            commonUser.setUserSecondLastName(commonUserDto.getUserSecondLastName());

            if (commonUserDto.getUserProfilePic() != null && !commonUserDto.getUserProfilePic().isEmpty()) {
                String imageUrl = firebaseStorageService.uploadFile(commonUserDto.getUserProfilePic(), "common-users-profile-pics/");
                commonUser.setUserProfilePicUrl(imageUrl);
            }

            Role role = roleRepository.findByRoleName(ROLE_COMMON_USER)
                    .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));

            commonUser.setRole(role);
            commonUser.setAccountStatus("Sin confirmar");
            String confirmationCode = String.format("%06d", new Random().nextInt(999999));
            commonUser.setConfirmationCode(confirmationCode);
            commonUser.setConfirmationCodeExpiry(LocalDateTime.now().plusDays(1));
            commonUser = commonUserRepository.save(commonUser);
            String emailContent = "Tu código de confirmación es: " + confirmationCode;
            emailService.sendConfirmationEmail(commonUser.getUserEmail(), "Confirmación de tu cuenta", emailContent);
            return new Response<>(commonUser.getCommonUserId(), false, 200, "La cuenta de usuario ha sido creada con éxito. Revisa tu correo para confirmarla.");
        } catch (RuntimeException e) {
            return new Response<>(true, 200, "Hubo un error creando la cuenta de usuario: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Long> updateCommonUserInfo(Long userId, CommonUserDto commonUserDto) {
        CommonUser commonUser = commonUserRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        commonUser.setUserCellphone(commonUserDto.getUserCellphone());
        commonUser.setUserEmail(commonUserDto.getUserEmail());
        commonUser.setUserFirstLastName(commonUserDto.getUserFirstLastName());
        commonUser.setUserName(commonUserDto.getUserName());
        commonUser.setUserSecondLastName(commonUserDto.getUserSecondLastName());
        commonUserRepository.save(commonUser);
        return new Response<>(commonUser.getCommonUserId(), false, 200, "Información del usuario actualizada con éxito.");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<String> updateCommonUserProfilePic(Long userId, MultipartFile userProfilePic) throws IOException {
        CommonUser commonUser = commonUserRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        if (!userProfilePic.isEmpty()) {
            if (commonUser.getUserProfilePicUrl() != null && !commonUser.getUserProfilePicUrl().isEmpty()) {
                try {
                    firebaseStorageService.deleteFileFromFirebase(commonUser.getUserProfilePicUrl(), "common-users-profile-pics/");
                } catch (IOException e) {
                    e.printStackTrace();
                    return new Response<>(true, 500, "Error al eliminar la foto de perfil anterior: " + e.getMessage());
                }
            }
            String imageUrl = firebaseStorageService.uploadFile(userProfilePic, "common-users-profile-pics/");
            commonUser.setUserProfilePicUrl(imageUrl);
            commonUserRepository.save(commonUser);
            return new Response<>(imageUrl, false, 200, "Foto de perfil actualizada con éxito.");
        } else {
            return new Response<>(true, 400, "La foto de perfil no puede estar vacía.");
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Long> createAdministratorAccount(AdministratorDto administratorDto) {
        try {
            Administrator administrator = new Administrator();
            administrator.setAdminCellphone(administratorDto.getAdminCellphone());
            administrator.setAdminEmail(administratorDto.getAdminEmail());
            administrator.setAdminFirstLastName(administratorDto.getAdminFirstLastName());
            administrator.setAdminName(administratorDto.getAdminName());
            administrator.setAdminPassword(passwordEncoder.encode(administratorDto.getAdminPassword()));
            administrator.setAdminSecondLastName(administratorDto.getAdminSecondLastName());
            administrator.setAdminSalary(administratorDto.getAdminSalary());
            administrator.setAdminSecurityNumber(administratorDto.getAdminSecurityNumber());

            if (administratorDto.getAdminProfilePic() != null && !administratorDto.getAdminProfilePic().isEmpty()) {
                String imageUrl = firebaseStorageService.uploadFile(administratorDto.getAdminProfilePic(), "admins-profile-pics/");
                administrator.setAdminProfilePicUrl(imageUrl);
            }

            Role role = roleRepository.findByRoleName(ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
            administrator.setRole(role);
            administrator = administratorRepository.save(administrator);
            return new Response<>(administrator.getAdminId(),false, 200, "La cuenta de adminstrador ha sido creada con éxito");

        }catch (RuntimeException e) {
            return new Response<>(true, 200, "Hubo un error creando la cuenta de administrador: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Long> updateAdminInfo(Long adminId, AdministratorDto administratorDto) {
        Administrator administrator = administratorRepository.findById(adminId)
                .orElseThrow(() -> new UsernameNotFoundException("Administrador no encontrado"));
        administrator.setAdminCellphone(administratorDto.getAdminCellphone());
        administrator.setAdminName(administratorDto.getAdminName());
        administrator.setAdminFirstLastName(administratorDto.getAdminFirstLastName());
        administrator.setAdminSecondLastName(administratorDto.getAdminSecondLastName());
        administrator.setAdminSecurityNumber(administratorDto.getAdminSecurityNumber());
        administrator.setAdminSalary(administratorDto.getAdminSalary());
        administrator.setAdminEmail(administratorDto.getAdminEmail());
        administratorRepository.save(administrator);
        return new Response<>(administrator.getAdminId(), false, 200, "Información del administrador actualizada con éxito.");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<String> updateAdminProfilePic(Long adminId, MultipartFile adminProfilePic) throws IOException {
        Administrator administrator = administratorRepository.findById(adminId)
                .orElseThrow(() -> new UsernameNotFoundException("Administrador no encontrado"));
        if (!adminProfilePic.isEmpty()) {
            if (administrator.getAdminProfilePicUrl() != null && !administrator.getAdminProfilePicUrl().isEmpty()) {
                try {
                    firebaseStorageService.deleteFileFromFirebase(administrator.getAdminProfilePicUrl(), "admins-profile-pics/");
                } catch (IOException e) {
                    e.printStackTrace();
                    return new Response<>(true, 500, "Error al eliminar la foto de perfil anterior: " + e.getMessage());
                }
            }
            String imageUrl = firebaseStorageService.uploadFile(adminProfilePic, "admins-profile-pics/");
            administrator.setAdminProfilePicUrl(imageUrl);
            administratorRepository.save(administrator);
            return new Response<>(imageUrl, false, 200, "Foto de perfil actualizada con éxito.");
        } else {
            return new Response<>(true, 400, "La foto de perfil no puede estar vacía.");
        }
    }

    public List<AdminGiveInfoDto> findAllAdministrators() {
        List<Administrator> administrators = administratorRepository.findAllByRoleName(ROLE_ADMIN);
        return administrators.stream().map(this::convertToAdminDto).toList();
    }

    private AdminGiveInfoDto convertToAdminDto(Administrator administrator) {
        AdminGiveInfoDto dto = new AdminGiveInfoDto(administrator);
        dto.setAdminName(administrator.getAdminName());
        dto.setAdminFirstLastName(administrator.getAdminFirstLastName());
        dto.setAdminEmail(administrator.getAdminEmail());
        dto.setAdminCellphone(administrator.getAdminCellphone());
        return dto;
    }

    public List<WorkerGiveInfoDto> findAllWorkers() {
        List<Worker> workers = workerRepository.findAllByRoleName(ROLE_WORKER);
        return workers.stream().map(this::convertToWorkerDto).toList();
    }

    private WorkerGiveInfoDto convertToWorkerDto(Worker worker) {
        WorkerGiveInfoDto dto = new WorkerGiveInfoDto(worker);
        dto.setWorkerName(worker.getWorkerName());
        dto.setWorkerFirstLastName(worker.getWorkerFirstLastName());
        dto.setWorkerSecondLastName(worker.getWorkerSecondLastName());
        dto.setWorkerCellphone(worker.getWorkerCellphone());
        dto.setWorkerEmail(worker.getWorkerEmail());
        dto.setWorkerRfc(worker.getWorkerRfc());
        return dto;
    }
    @Transactional(rollbackFor = {SQLException.class})
    public Response<Long> createWorkerAccount(WorkerDto workerDto) {
        try {
            Worker worker = new Worker();
            worker.setWorkerCellphone(workerDto.getWorkerCellphone());
            worker.setWorkerEmail(workerDto.getWorkerEmail());
            worker.setWorkerFirstLastName(workerDto.getWorkerFirstLastName());
            worker.setWorkerName(workerDto.getWorkerName());
            worker.setWorkerPassword(passwordEncoder.encode(workerDto.getWorkerPassword()));
            worker.setWorkerSecondLastName(workerDto.getWorkerSecondLastName());
            worker.setWorkerSalary(workerDto.getWorkerSalary());
            worker.setWorkerSecurityNumber(workerDto.getWorkerSecurityNumber());
            worker.setWorkerRfc(workerDto.getWorkerRfc());

            if (workerDto.getWorkerProfilePic() != null && !workerDto.getWorkerProfilePic().isEmpty()) {
                String imageUrl = firebaseStorageService.uploadFile(workerDto.getWorkerProfilePic(), "workers-profile-pics/");
                worker.setWorkerProfilePicUrl(imageUrl);
            }

            Role role = roleRepository.findByRoleName(ROLE_WORKER)
                    .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
            worker.setRole(role);
            worker = workerRepository.save(worker);
            return new Response<>(worker.getWorkerId(), false, 200, "La cuenta de trabajador ha sido creada con éxito");
        }catch (RuntimeException e) {
            return new Response<>(true, 200, "Hubo un error creando la cuenta de trabajador: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Long> updateWorkerInfo(Long workerId, WorkerDto workerDto) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new UsernameNotFoundException("Trabajador no encontrado"));
        worker.setWorkerName(workerDto.getWorkerName());
        worker.setWorkerFirstLastName(workerDto.getWorkerFirstLastName());
        worker.setWorkerSecondLastName(workerDto.getWorkerSecondLastName());
        worker.setWorkerEmail(workerDto.getWorkerEmail());
        worker.setWorkerCellphone(workerDto.getWorkerCellphone());
        worker.setWorkerSecurityNumber(workerDto.getWorkerSecurityNumber());
        worker.setWorkerSalary(worker.getWorkerSalary());
        worker.setWorkerRfc(worker.getWorkerRfc());
        workerRepository.save(worker);
        return new Response<>(worker.getWorkerId(), false, 200, "Información del trabajador actualizada con éxito.");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<String> updateWorkerProfilePic(Long workerId, MultipartFile workerProfilePic) throws IOException {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new UsernameNotFoundException("Trabajador no encontrado"));
        if (!workerProfilePic.isEmpty()) {
            if (worker.getWorkerProfilePicUrl() != null && !worker.getWorkerProfilePicUrl().isEmpty()) {
                try {
                    firebaseStorageService.deleteFileFromFirebase(worker.getWorkerProfilePicUrl(), "workers-profile-pics/");
                } catch (IOException e) {
                    e.printStackTrace();
                    return new Response<>(true, 500, "Error al eliminar la foto de perfil anterior: " + e.getMessage());
                }
            }
            String imageUrl = firebaseStorageService.uploadFile(workerProfilePic, "workers-profile-pics/");
            worker.setWorkerProfilePicUrl(imageUrl);
            workerRepository.save(worker);
            return new Response<>(imageUrl, false, 200, "Foto de perfil actualizada con éxito.");
        } else {
            return new Response<>(true, 400, "La foto de perfil no puede estar vacía.");
        }
    }

    public Object getLoggedUserProfile(String username, String role) {
        switch (role) {
            case ROLE_ADMIN -> {
                Administrator admin = administratorRepository.findByAdminName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Admin no encontrado"));
                return new AdminGiveInfoDto(admin);
            }
            case ROLE_WORKER -> {
                Worker worker = workerRepository.findByWorkerName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Trabajador no encontrado"));
                return new WorkerGiveInfoDto(worker);
            }
            case ROLE_COMMON_USER -> {
                CommonUser commonUser = commonUserRepository.findByUserName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario común no encontrado"));
                return new CommonUserGiveInfoDto(commonUser);
            }
            default -> throw new IllegalStateException("Tipo de usuario desconocido");
        }
    }
}
