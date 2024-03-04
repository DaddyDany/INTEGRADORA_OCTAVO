package utez.edu.mx.orderApp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderApp.Models.Role;
import utez.edu.mx.orderApp.Services.RoleService;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:5173")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/")
    public ResponseEntity getAll() {
        return new ResponseEntity(
                this.roleService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{roleId}")
    public ResponseEntity getOne(
            @PathVariable("roleId") Long id
    ) {
        return new ResponseEntity(
                this.roleService.getOne(id),
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity save(
            @RequestBody(required = true) Role role
    ) {
        return new ResponseEntity(
                this.roleService.insert(role),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity update(
            @RequestBody(required = true) Role role
    ) {
        return new ResponseEntity(
                this.roleService.update(role),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity delete(
            @PathVariable("roleId") Long id
    ) {
        return new ResponseEntity(
                this.roleService.delete(id),
                HttpStatus.OK
        );
    }
}
