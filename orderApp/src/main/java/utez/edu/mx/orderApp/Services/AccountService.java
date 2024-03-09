package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderApp.Controllers.Accounts.AccountDto;
import utez.edu.mx.orderApp.Models.Accounts.Administrator;
import utez.edu.mx.orderApp.Models.Accounts.CommonUser;
import utez.edu.mx.orderApp.Models.Accounts.Role;
import utez.edu.mx.orderApp.Models.Accounts.UserAttribute;
import utez.edu.mx.orderApp.Models.Accounts.Worker;
import utez.edu.mx.orderApp.Repositories.Accounts.AdministratorRepository;
import utez.edu.mx.orderApp.Repositories.Accounts.CommonUserRepository;
import utez.edu.mx.orderApp.Repositories.Accounts.RoleRepository;
import utez.edu.mx.orderApp.Repositories.Accounts.UserAttributeRepository;
import utez.edu.mx.orderApp.Repositories.Accounts.WorkerRepository;
import utez.edu.mx.orderApp.Utils.Response;

@Service
public class AccountService {
    @Autowired
    private UserAttributeRepository userAttributeRepository;
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
    public Response createAccount(AccountDto accountDto) {
        try {
            UserAttribute userAttributes = new UserAttribute();
            userAttributes.setUserCellphone(accountDto.getUserCellphone());
            userAttributes.setUserEmail(accountDto.getUserEmail());
            userAttributes.setUserFirstLastName(accountDto.getUserFirstLastName());
            userAttributes.setUserName(accountDto.getUserName());
            userAttributes.setUserPassword(passwordEncoder.encode(accountDto.getUserPassword()));
            userAttributes.setUserSecondLastName(accountDto.getUserSecondLastName());
            Role role = roleRepository.findById(accountDto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            userAttributes.setRole(role);

            userAttributes = userAttributeRepository.save(userAttributes);

            switch (accountDto.getRoleId().intValue()) {
                case 1 -> {
                    CommonUser commonUser = new CommonUser();
                    commonUser.setUserAttribute(userAttributes);
                    commonUserRepository.save(commonUser);
                }
                case 2 -> {
                    Worker worker = new Worker();
                    worker.setUserAttribute(userAttributes);
                    workerRepository.save(worker);
                }
                case 3 -> {
                    Administrator administrator = new Administrator();
                    administrator.setUserAttribute(userAttributes);
                    administratorRepository.save(administrator);
                }
                default -> throw new IllegalArgumentException("Rol no valido");
            }
            return new Response(
                    userAttributes.getUserAttributesId(),
                    false,
                    200,
                    "La cuenta ha sido creada con exito"
            );
        }catch (RuntimeException e) {
            return new Response(
                    null,
                    true,
                    400,
                    "Hubo un error creando la cuenta " +
                            e.getMessage()
            );
        }
    }
    @Transactional
    public Response deleteAccount(Long userAttributeId) {
        if (this.userAttributeRepository.existsById(userAttributeId)){
            userAttributeRepository.deleteById(userAttributeId);
            return new Response(
                    null, 
                    false,
                    200,
                    "Cuenta eliminada Correctamente"
            );
        }else {
            return new Response(
                    null,
                    true,
                    400,
                    "Cuenta no encontrada, error al eliminar la cuenta"
            );
        }
    }
}
