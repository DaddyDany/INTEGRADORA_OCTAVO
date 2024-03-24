package utez.edu.mx.orderapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderapp.repositories.accounts.AdministratorRepository;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.repositories.accounts.WorkerRepository;
import utez.edu.mx.orderapp.security.jwt.JwtProvider;
import utez.edu.mx.orderapp.utils.ApiResponse;

@Service
@Transactional
public class AuthService {

    private final CommonUserRepository commonUserRepository;
    private final WorkerRepository workerRepository;
    private final AdministratorRepository administratorRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthService(CommonUserRepository commonUserRepository, WorkerRepository workerRepository,
                       AdministratorRepository administratorRepository, AuthenticationManager authenticationManager,
                       JwtProvider jwtProvider) {
        this.commonUserRepository = commonUserRepository;
        this.workerRepository = workerRepository;
        this.administratorRepository = administratorRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public ResponseEntity<ApiResponse> signIn(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generateToken(authentication);

            return ResponseEntity.ok(new ApiResponse(jwt, "Authentication successful", HttpStatus.OK, false));
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, "Authentication failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED, true), HttpStatus.UNAUTHORIZED);
        }
    }
}