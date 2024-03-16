package utez.edu.mx.orderApp.Security.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utez.edu.mx.orderApp.Models.Accounts.CommonUser;
import utez.edu.mx.orderApp.Models.Accounts.Administrator;
import utez.edu.mx.orderApp.Models.Accounts.Worker;
import utez.edu.mx.orderApp.Repositories.Accounts.CommonUserRepository;
import utez.edu.mx.orderApp.Repositories.Accounts.AdministratorRepository;
import utez.edu.mx.orderApp.Repositories.Accounts.WorkerRepository;
import utez.edu.mx.orderApp.Security.Entity.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CommonUserRepository commonUserRepository;
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CommonUser commonUser = commonUserRepository.findByUserName(username).orElse(null);
        if (commonUser != null) {
            return UserDetailsImpl.fromCommonUser(commonUser);
        }

        Worker worker = workerRepository.findByWorkerName(username).orElse(null);
        if (worker != null) {
            return UserDetailsImpl.fromWorker(worker);
        }

        Administrator administrator = administratorRepository.findByAdminName(username).orElse(null);
        if (administrator != null) {
            return UserDetailsImpl.fromAdministrator(administrator);
        }

        throw new UsernameNotFoundException("User Not Found with username: " + username);
    }
}