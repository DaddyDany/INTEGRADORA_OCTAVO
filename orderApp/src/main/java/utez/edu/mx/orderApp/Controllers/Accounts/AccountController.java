package utez.edu.mx.orderApp.Controllers.Accounts;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderApp.Models.Accounts.UserAttribute;
import utez.edu.mx.orderApp.Services.AccountService;
import utez.edu.mx.orderApp.Utils.Response;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<UserAttribute> createAccount(@RequestBody AccountDto accountDto) {
        try{
            Response createdAccount = accountService.createAccount(accountDto);
            return new ResponseEntity(
                    createdAccount,
                    HttpStatus.CREATED
            );
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try{
            accountService.deleteAccount(id);
            return new ResponseEntity(
                    this.accountService.deleteAccount(id),
                    HttpStatus.OK
            );
        }catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
