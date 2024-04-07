package utez.edu.mx.orderapp.services.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.mutable.Mutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import utez.edu.mx.orderapp.utils.EncryptionService;
import utez.edu.mx.orderapp.utils.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    private static final String ROLE_WORKER = "WORKER";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_COMMON_USER = "COMMON_USER";
    private static final String ROLE_NOT_FOUND = "Rol no encontrado";


    @Autowired
    public AccountService(RoleRepository roleRepository, CommonUserRepository commonUserRepository, WorkerRepository workerRepository, AdministratorRepository administratorRepository, PasswordEncoder passwordEncoder, EmailService emailService, FirebaseStorageService firebaseStorageService, EncryptionService encryptionService, ObjectMapper objectMapper){
        this.roleRepository = roleRepository;
        this.commonUserRepository = commonUserRepository;
        this.workerRepository = workerRepository;
        this.administratorRepository = administratorRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.firebaseStorageService = firebaseStorageService;
        this.encryptionService = encryptionService;
        this.objectMapper = objectMapper;
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Long> createCommonUserAccount(CommonUserDto commonUserDto) {
        try {
            CommonUser commonUser = new CommonUser();
            commonUser.setUserCellphone(commonUserDto.getUserCellphone());
            commonUser.setUserEmail(commonUserDto.getUserEmail());
            commonUser.setUserFirstLastName(commonUserDto.getUserFirstLastName());
            commonUser.setUserName(commonUserDto.getUserName());
            if (commonUser.getUserPassword() == null || commonUser.getUserPassword().isEmpty()){
                throw new IllegalArgumentException("La contraseña no puede estar vacía");
            }
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

    @Transactional(rollbackFor = {Exception.class})
    public Response<String> createAdministratorAccount(String encryptedData, MultipartFile adminProfilePic) throws Exception {
        String decryptedDataJson = encryptionService.decrypt(encryptedData);
        AdministratorDto adminDto = objectMapper.readValue(decryptedDataJson, AdministratorDto.class);
        Administrator administrator = new Administrator();
        administrator.setAdminCellphone(adminDto.getAdminCellphone());
        administrator.setAdminEmail(adminDto.getAdminEmail());
        administrator.setAdminFirstLastName(adminDto.getAdminFirstLastName());
        administrator.setAdminName(adminDto.getAdminName());
        if (adminDto.getAdminPassword() == null || adminDto.getAdminPassword().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        administrator.setAdminPassword(passwordEncoder.encode(adminDto.getAdminPassword()));
        administrator.setAdminSecondLastName(adminDto.getAdminSecondLastName());
        Long adminSalary = Long.parseLong(adminDto.getAdminSalary());
        administrator.setAdminSalary(adminSalary);
        administrator.setAdminSecurityNumber(adminDto.getAdminSecurityNumber());

        if (adminProfilePic != null && !adminProfilePic.isEmpty()) {
            String imageUrl = firebaseStorageService.uploadFile(adminProfilePic, "admins-profile-pics/");
            administrator.setAdminProfilePicUrl(imageUrl);
        }

        Role role = roleRepository.findByRoleName(ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
        administrator.setRole(role);
        administratorRepository.save(administrator);
        return new Response<>("Creado", false, 200, "La cuenta de administrador ha sido creada con éxito");
    }


    @Transactional(rollbackFor = {Exception.class})
    public Response<Long> createWorkerAccount(String encryptedData, MultipartFile workerProfilePic) throws Exception {
        String decryptedDataJson = encryptionService.decrypt(encryptedData);
        WorkerDto workerDto = objectMapper.readValue(decryptedDataJson, WorkerDto.class);
        Worker worker = new Worker();
        worker.setWorkerEmail(workerDto.getWorkerEmail());
        worker.setWorkerName(workerDto.getWorkerName());
        worker.setWorkerCellphone(workerDto.getWorkerCellphone());
        worker.setWorkerFirstLastName(workerDto.getWorkerFirstLastName());
        worker.setWorkerSecondLastName(workerDto.getWorkerSecondLastName());
        worker.setWorkerSecurityNumber(workerDto.getWorkerSecurityNumber());
        worker.setWorkerSalary(workerDto.getWorkerSalary());
        worker.setWorkerRfc(workerDto.getWorkerRfc());
        if (worker.getWorkerPassword() == null || worker.getWorkerPassword().isEmpty()){
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        worker.setWorkerPassword(passwordEncoder.encode(workerDto.getWorkerPassword()));
        if (workerProfilePic != null && !workerProfilePic.isEmpty()) {
            String imageUrl = firebaseStorageService.uploadFile(workerProfilePic, "workers-profile-pics/");
            worker.setWorkerProfilePicUrl(imageUrl);
        }
        Role role = roleRepository.findByRoleName(ROLE_WORKER)
                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
        worker.setRole(role);
        worker = workerRepository.save(worker);
        return new Response<>(worker.getWorkerId(), false, 200, "La cuenta de trabajador ha sido creada con éxito");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<String> updateAdminInfo(String encryptedData) throws Exception {

        String decryptedDataJson = encryptionService.decrypt(encryptedData);
        AdministratorDto adminDto = objectMapper.readValue(decryptedDataJson, AdministratorDto.class);

        System.out.println("El id es: " + adminDto.getAdminId());

        Long adminId = Long.parseLong(adminDto.getAdminId());


        Administrator administrator = administratorRepository.findById(adminId)
                .orElseThrow(() -> new UsernameNotFoundException("Administrador no encontrado"));

        administrator.setAdminCellphone(adminDto.getAdminCellphone());
        administrator.setAdminName(adminDto.getAdminName());
        administrator.setAdminFirstLastName(adminDto.getAdminFirstLastName());
        administrator.setAdminSecondLastName(adminDto.getAdminSecondLastName());
        administrator.setAdminSecurityNumber(adminDto.getAdminSecurityNumber());
        Long adminSalary = Long.parseLong(adminDto.getAdminSalary());
        administrator.setAdminSalary(adminSalary);
        administratorRepository.save(administrator);
        return new Response<>("Admin actualizado", false, 200, "Información del administrador actualizada con éxito.");
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
        return administrators.stream().map(administrator -> {
           try{
               return convertToAdminDto(administrator);
           }catch (Exception e){
               return null;
           }
        }).filter(Objects::nonNull).toList();
    }

    private AdminGiveInfoDto convertToAdminDto(Administrator administrator) throws  Exception {
        AdminGiveInfoDto dto = new AdminGiveInfoDto(administrator);
        String encryptedId = encryptionService.encrypt(String.valueOf(administrator.getAdminId()));
        dto.setAdminId(encryptedId);
        dto.setAdminName(encryptionService.encrypt(administrator.getAdminName()));
        dto.setAdminFirstLastName(encryptionService.encrypt(administrator.getAdminFirstLastName()));
        dto.setAdminSecondLastName(encryptionService.encrypt(administrator.getAdminSecondLastName()));
        dto.setAdminSecurityNumber(encryptionService.encrypt(administrator.getAdminSecurityNumber()));
        dto.setAdminProfilePicUrl(encryptionService.encrypt(administrator.getAdminProfilePicUrl()));
        String encryptedSalary = encryptionService.encrypt(String.valueOf(administrator.getAdminSalary()));
        dto.setAdminSalary(encryptedSalary);
        dto.setAdminEmail(encryptionService.encrypt(administrator.getAdminEmail()));
        dto.setAdminCellphone(encryptionService.encrypt(administrator.getAdminCellphone()));
        return dto;
    }

    @Transactional(readOnly = true)
    public List<WorkerGiveInfoDto> findAllWorkers() {
        List<Worker> workers = workerRepository.findAllByRoleName(ROLE_WORKER);
        return workers.stream()
                .map(worker -> {
                    try {
                        return convertToWorkerDto(worker);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private WorkerGiveInfoDto convertToWorkerDto(Worker worker) throws Exception {
        WorkerGiveInfoDto dto = new WorkerGiveInfoDto(worker);
        String encryptedId = encryptionService.encrypt(String.valueOf(worker.getWorkerId()));
        dto.setWorkerId(encryptedId);
        dto.setWorkerName(encryptionService.encrypt(worker.getWorkerName()));
        dto.setWorkerFirstLastName(encryptionService.encrypt(worker.getWorkerFirstLastName()));
        dto.setWorkerSecondLastName(encryptionService.encrypt(worker.getWorkerSecondLastName()));
        dto.setWorkerCellphone(encryptionService.encrypt(worker.getWorkerCellphone()));
        dto.setWorkerEmail(encryptionService.encrypt(worker.getWorkerEmail()));
        dto.setWorkerRfc(encryptionService.encrypt(worker.getWorkerRfc()));
        String encryptedSalary = encryptionService.encrypt(String.valueOf(worker.getWorkerSalary()));
        dto.setWorkerSalary(encryptedSalary);
        String encryptedSecurityNumber = encryptionService.encrypt(String.valueOf(worker.getWorkerSecurityNumber()));
        dto.setWorkerSecurityNumber(encryptedSecurityNumber);
        dto.setWorkerProfilePicUrl(encryptionService.encrypt(worker.getWorkerProfilePicUrl()));
        return dto;
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Long> updateWorkerInfo(Long workerId, String encryptedData) throws Exception {
        System.out.println("Before Decryption: " + encryptedData);
        String decryptedDataJson = encryptionService.decrypt(encryptedData);
        System.out.println(decryptedDataJson);
        WorkerDto workerDto = objectMapper.readValue(decryptedDataJson, WorkerDto.class);

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new UsernameNotFoundException("Trabajador no encontrado"));
        worker.setWorkerName(workerDto.getWorkerName());
        worker.setWorkerFirstLastName(workerDto.getWorkerFirstLastName());
        worker.setWorkerSecondLastName(workerDto.getWorkerSecondLastName());
        worker.setWorkerCellphone(workerDto.getWorkerCellphone());
        worker.setWorkerSecurityNumber(workerDto.getWorkerSecurityNumber());
        worker.setWorkerSalary(workerDto.getWorkerSalary());
        worker.setWorkerRfc(workerDto.getWorkerRfc());
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
                Administrator admin = administratorRepository.findByAdminEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Admin no encontrado"));
                return new AdminGiveInfoDto(admin);
            }
            case ROLE_WORKER -> {
                Worker worker = workerRepository.findByWorkerEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Trabajador no encontrado"));
                return new WorkerGiveInfoDto(worker);
            }
            case ROLE_COMMON_USER -> {
                CommonUser commonUser = commonUserRepository.findByUserEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario común no encontrado"));
                return new CommonUserGiveInfoDto(commonUser);
            }
            default -> throw new IllegalStateException("Tipo de usuario desconocido");
        }
    }
}
