package utez.edu.mx.orderApp.Repositories.Accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Accounts.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> { }
