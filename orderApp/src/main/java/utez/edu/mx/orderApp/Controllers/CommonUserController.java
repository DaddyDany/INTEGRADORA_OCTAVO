package utez.edu.mx.orderApp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderApp.Services.CommonUserService;

@RestController
@RequestMapping("/api/common-users")
@CrossOrigin(origins = "http://localhost:5173")
public class CommonUserController {
    @Autowired
    private CommonUserService commonUserService;

    @GetMapping("/")
    public ResponseEntity getAll() {
        return new ResponseEntity(
                this.commonUserService.getAll(),
                HttpStatus.OK
        );
    }
}