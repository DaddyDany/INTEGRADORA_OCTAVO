package utez.edu.mx.orderapp.controllers.authentications;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.orderapp.controllers.authentications.dtos.SignDto;
import utez.edu.mx.orderapp.services.auths.AuthService;
import utez.edu.mx.orderapp.utils.ApiResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignDto dto) {
        return service.signIn(dto.getUsername(), dto.getPassword());
    }
}