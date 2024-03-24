package utez.edu.mx.orderapp.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.accounts.Administrator;
import utez.edu.mx.orderapp.models.accounts.Worker;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.repositories.accounts.AdministratorRepository;
import utez.edu.mx.orderapp.repositories.accounts.WorkerRepository;
import utez.edu.mx.orderapp.security.entity.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final CommonUserRepository commonUserRepository;
    private final AdministratorRepository administratorRepository;
    private final WorkerRepository workerRepository;
    @Autowired
    public UserDetailsServiceImpl(CommonUserRepository commonUserRepository, AdministratorRepository administratorRepository, WorkerRepository workerRepository){
        this.administratorRepository = administratorRepository;
        this.commonUserRepository = commonUserRepository;
        this.workerRepository = workerRepository;
    }

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