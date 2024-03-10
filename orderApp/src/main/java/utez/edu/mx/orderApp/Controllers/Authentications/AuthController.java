package utez.edu.mx.orderApp.Controllers.Authentications;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.orderApp.Services.AuthService;
import utez.edu.mx.orderApp.Utils.ApiResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final AuthService service;

    //testing comment

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignDto dto) {
        return service.signIn(dto.getUsername(), dto.getPassword());
    }
}