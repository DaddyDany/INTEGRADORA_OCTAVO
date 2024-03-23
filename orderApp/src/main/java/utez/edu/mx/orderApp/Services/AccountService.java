package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderApp.Controllers.Accounts.Dtos.AdministratorDto;
import utez.edu.mx.orderApp.Controllers.Accounts.Dtos.AdministratorToAdminDto;
import utez.edu.mx.orderApp.Controllers.Accounts.Dtos.CommonUserDto;
import utez.edu.mx.orderApp.Controllers.Accounts.Dtos.WorkerDto;
import utez.edu.mx.orderApp.Controllers.Accounts.Dtos.WorkerToAdminDto;
import utez.edu.mx.orderApp.Models.Accounts.Administrator;
import utez.edu.mx.orderApp.Models.Accounts.CommonUser;
import utez.edu.mx.orderApp.Models.Accounts.Role;
import utez.edu.mx.orderApp.Models.Accounts.Worker;
import utez.edu.mx.orderApp.Repositories.Accounts.AdministratorRepository;
import utez.edu.mx.orderApp.Repositories.Accounts.CommonUserRepository;
import utez.edu.mx.orderApp.Repositories.Accounts.RoleRepository;
import utez.edu.mx.orderApp.Repositories.Accounts.WorkerRepository;
import utez.edu.mx.orderApp.Utils.Response;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CommonUserRepository commonUserRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Transactional
    public Response createCommonUserAccount(CommonUserDto commonUserDto) {
        try {
            CommonUser commonUser = new CommonUser();
            commonUser.setUserCellphone(commonUserDto.getUserCellphone());
            commonUser.setUserEmail(commonUserDto.getUserEmail());
            commonUser.setUserFirstLastName(commonUserDto.getUserFirstLastName());
            commonUser.setUserName(commonUserDto.getUserName());
            commonUser.setUserPassword(passwordEncoder.encode(commonUserDto.getUserPassword()));
            commonUser.setUserSecondLastName(commonUserDto.getUserSecondLastName());
            Role role = roleRepository.findByRoleName("COMMON_USER")
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            commonUser.setRole(role);
            commonUser = commonUserRepository.save(commonUser);
            return new Response(
                    commonUser.getCommonUserId(),
                    false,
                    200,
                    "La cuenta de usuario ha sido creada con exito"
            );
        }catch (RuntimeException e) {
            return new Response(
                    null,
                    true,
                    400,
                    "Hubo un error creando la cuenta de usuario" +
                            e.getMessage()
            );
        }
    }

    @Transactional
    public Response createAdministratorAccount(AdministratorDto administratorDto) {
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
            Role role = roleRepository.findByRoleName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            administrator.setRole(role);
            administrator = administratorRepository.save(administrator);
            return new Response(
                    administrator.getAdminId(),
                    false,
                    200,
                    "La cuenta de administrador ha sido creada con exito"
            );
        }catch (RuntimeException e) {
            return new Response(
                    null,
                    true,
                    400,
                    "Hubo un error creando la cuenta de administrador" +
                            e.getMessage()
            );
        }
    }

    public List<AdministratorToAdminDto> findAllAdministrators() {
        List<Administrator> administrators = administratorRepository.findAllByRoleName("ADMIN");
        return administrators.stream().map(this::convertToAdminDto).collect(Collectors.toList());
    }

    private AdministratorToAdminDto convertToAdminDto(Administrator administrator) {
        AdministratorToAdminDto dto = new AdministratorToAdminDto();
        dto.setAdminName(administrator.getAdminName());
        dto.setAdminFirstLastName(administrator.getAdminFirstLastName());
        dto.setAdminEmail(administrator.getAdminEmail());
        dto.setAdminCellphone(administrator.getAdminCellphone());
        return dto;
    }

    public List<WorkerToAdminDto> findAllWorkers() {
        List<Worker> workers = workerRepository.findAllByRoleName("WORKER");
        return workers.stream().map(this::convertToWorkerDto).collect(Collectors.toList());
    }

    private WorkerToAdminDto convertToWorkerDto(Worker worker) {
        WorkerToAdminDto dto = new WorkerToAdminDto();
        dto.setWorkerName(worker.getWorkerName());
        dto.setWorkerFirstLastName(worker.getWorkerFirstLastName());
        dto.setWorkerSecondLastName(worker.getWorkerSecondLastName());
        dto.setWorkerCellphone(worker.getWorkerCellphone());
        dto.setWorkerEmail(worker.getWorkerEmail());
        dto.setWorkerRfc(worker.getWorkerRfc());
        return dto;
    }

    @Transactional
    public Response createWorkerAccount(WorkerDto workerDto) {
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
            Role role = roleRepository.findByRoleName("WORKER")
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            worker.setRole(role);
            worker = workerRepository.save(worker);
            return new Response(
                    worker.getWorkerId(),
                    false,
                    200,
                    "La cuenta de trabajador ha sido creada con exito"
            );
        }catch (RuntimeException e) {
            return new Response(
                    null,
                    true,
                    400,
                    "Hubo un error creando la cuenta de trabajador" +
                            e.getMessage()
            );
        }
    }

}
