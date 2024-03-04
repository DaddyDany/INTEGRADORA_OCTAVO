package utez.edu.mx.orderApp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderApp.Services.UserAttributeService;

@RestController
@RequestMapping("/api/user-attributes")
@CrossOrigin(origins = "http://localhost:5173")
public class UserAttributeController {
    @Autowired
    private UserAttributeService userAttributeService;

    @GetMapping("/")
    public ResponseEntity getAll() {
        return new ResponseEntity(
                this.userAttributeService.getAll(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity(
                this.userAttributeService.delete(id),
                HttpStatus.OK
        );
    }
}
