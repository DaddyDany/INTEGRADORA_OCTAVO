package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderApp.Repositories.Accounts.UserAttributeRepository;
import utez.edu.mx.orderApp.Security.Jwt.JwtProvider;
import utez.edu.mx.orderApp.Utils.ApiResponse;

@Service
@Transactional
public class AuthService {
    private final UserAttributeRepository userAttributeRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthService(UserAttributeRepository userAttributeRepository, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userAttributeRepository = userAttributeRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public ResponseEntity<ApiResponse> signIn(String username, String password) {
        return userAttributeRepository.findByUserName(username)
                .map(user -> {
                    try {
                        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String token = jwtProvider.generateToken(authentication);

                        // Asumiendo que necesitas devolver el token y algunos datos del usuario
                        return ResponseEntity.ok(new ApiResponse(token, HttpStatus.OK));
                    } catch (DisabledException e) {
                        return new ResponseEntity<>(new ApiResponse("UserDisabled", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
                    } catch (Exception e) {
                        return new ResponseEntity<>(new ApiResponse("CredentialsMismatch", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
                    }
                })
                .orElseGet(() -> new ResponseEntity<>(new ApiResponse("UserNotFound", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));
    }
}
